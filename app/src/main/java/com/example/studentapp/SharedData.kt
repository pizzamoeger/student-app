package com.example.studentapp

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.studentapp.ui.classes.ClassesItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class SerializableClass(
    val id: Int,
    val name: String,
    val secondsToday: Int,
    val secondsTotal: Int
)


class SharedData  {
    companion object {
        private val _classesList = MutableLiveData<List<ClassesItem>>(emptyList())
        lateinit var prefs: SharedPreferences

        fun init(context: Context) {
            prefs = context.getSharedPreferences("shared_data_prefs", Context.MODE_PRIVATE)
            load()
        }

        // TODO save classes when exiting apps
        private var nextId = 0;
        private var _currentClass = MutableLiveData<ClassesItem?>()

        val classList: LiveData<List<ClassesItem>> get() = _classesList
        val currentClass: LiveData<ClassesItem?> get() = _currentClass

        fun addClass(name: String) {
            val newList = _classesList.value.orEmpty() + ClassesItem(nextId++, name, 0, 0)
            _classesList.value = newList
        }

        fun deleteClass(id: Int) {
            _classesList.value = _classesList.value?.filterNot { it.id == id }
        }

        fun switchClass(item: ClassesItem): Boolean {
            // returns true if the class changed
            if (_currentClass.value != null) _currentClass.value!!.update_tracking()
            if (_currentClass.value == item) return false
            item.update_tracking()
            _currentClass.value = item
            return true
        }

        fun save() {
            val gson = Gson()
            val serializableList = _classesList.value.orEmpty().map {
                SerializableClass(it.id, it.name, it.secondsToday, it.secondsTotal)
            }
            val json: String = gson.toJson(serializableList)
            prefs.edit().putString("classes_list", json).apply()
        }

        fun load() {
            val gson = Gson()
            val json = prefs.getString("classes_list", null)

            if (json != null) {
                val type = object : TypeToken<List<SerializableClass>>() {}.type
                val list: List<SerializableClass> = gson.fromJson(json, type)

                val restored = list.map {
                    ClassesItem(it.id, it.name, it.secondsToday, it.secondsTotal)
                }
                _classesList.value = restored
                nextId = (list.maxOfOrNull { it.id } ?: 0) + 1
            }
        }
    }
}