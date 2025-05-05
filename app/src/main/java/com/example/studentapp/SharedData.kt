package com.example.studentapp

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.studentapp.ui.classesItem.ClassesItem
import com.example.studentapp.ui.classesItem.SerializableClassesItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate

class SharedData  {
    companion object {
        private val _classesList = MutableLiveData<List<ClassesItem>>(emptyList())
        lateinit var prefs: SharedPreferences

        // is called once when app is created
        fun init(context: Context) {
            prefs = context.getSharedPreferences("shared_data_prefs", Context.MODE_PRIVATE)
            updateDate()
            load()
        }

        private var nextId = 0;
        private val _currentClass = MutableLiveData<ClassesItem?>()
        private val _today = MutableLiveData<LocalDate> ()

        val classList: LiveData<List<ClassesItem>> get() = _classesList
        val currentClass: LiveData<ClassesItem?> get() = _currentClass
        val today: LiveData<LocalDate> get() = _today

        // add class to classList
        fun addClass(name: String) {
            val newList = _classesList.value.orEmpty() + ClassesItem(nextId++, name, mutableMapOf())
            _classesList.value = newList
            save()
        }

        // delete class by id from classList
        fun deleteClass(id: Int) {
            _classesList.value = _classesList.value?.filterNot { it.id == id }
            save()
        }

        // switch currentClass to item
        fun switchClass(item: ClassesItem): Boolean {
            // returns true if the class changed
            if (_currentClass.value == item) {
                item.updateTracking(!item.tracking.value!!)
                return false
            }
            if (_currentClass.value != null) {
                _currentClass.value!!.updateTracking(false)
            }
            item.updateTracking(true)
            _currentClass.value = item
            return true
        }

        // save to sharedPreferences
        fun save() {
            val gson = Gson()

            // create a serializable list from classeslist
            val serializableList = _classesList.value.orEmpty().map {
                SerializableClassesItem(it.id, it.name, it.studyTime)
            }

            val json: String = gson.toJson(serializableList)
            prefs.edit().putString("classes_list", json).apply()
        }

        // load from sharedPreferences
        fun load() {
            val gson = Gson()
            val json = prefs.getString("classes_list", null)

            if (json != null) {
                // load list of serializableClass
                val type = object : TypeToken<List<SerializableClassesItem>>() {}.type
                val list: List<SerializableClassesItem> = gson.fromJson(json, type)

                // create list of ClassesItem from this
                val restored = list.map {
                    ClassesItem(it.id, it.name, it.studyTime)
                }
                
                _classesList.value = restored
                nextId = (list.maxOfOrNull { it.id } ?: 0) + 1
            }
        }

        // TODO call this once at midnight
        // but then if user sets time manually it breaks
        fun updateDate() {
            if (LocalDate.now().toString() != _today.value.toString()) {
                _today.value = LocalDate.now()
            }
        }
    }
}