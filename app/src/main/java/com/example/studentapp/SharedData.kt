package com.example.studentapp

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.studentapp.ui.classesItem.ClassesItem
import com.example.studentapp.ui.classesItem.SerializableClassesItem
import com.example.studentapp.ui.event.Event
import com.example.studentapp.ui.event.SerializableEvent
import com.example.studentapp.ui.getThemeColor
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate
import java.time.LocalTime

enum class TimeInterval {
    DAY, WEEK, MONTH, TOTAL, DEFAULT
}

class SharedData  {
    companion object {
        private val _classesList = MutableLiveData<List<ClassesItem>>(emptyList())
        lateinit var prefs: SharedPreferences

        var locked = false

        // is called once when app is created
        fun init(context: Context) {
            prefs = context.getSharedPreferences("shared_data_prefs", Context.MODE_PRIVATE)
            prefs.edit().clear().apply()
            defaultClass.color = context.getThemeColor(R.color.transparent)
            updateDate()
            load()
        }

        private var nextId = 1;
        private val _currentClass = MutableLiveData<ClassesItem?>()
        private val _today = MutableLiveData<LocalDate> ()

        val classList: LiveData<List<ClassesItem>> get() = _classesList
        val currentClass: LiveData<ClassesItem?> get() = _currentClass
        val today: LiveData<LocalDate> get() = _today
        var defaultClass = ClassesItem(0, "", mutableMapOf(), 0)

        // add class to classList
        fun addClass(name: String, color : Int) : ClassesItem {
            val newClass = ClassesItem(nextId++, name, mutableMapOf(), color)
            val newList = _classesList.value.orEmpty() + newClass
            _classesList.value = newList
            saveClass()
            return newClass
        }

        // delete class by id from classList
        fun deleteClass(id: Int) {
            _classesList.value = _classesList.value?.filterNot { it.id == id }
            Event.removeAllOfClass(id)
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

        fun save() {
            saveClass()
            saveEvent()
        }

        fun saveClass() {
            val gson = Gson()

            // create a serializable list from classeslist
            val serializableList = _classesList.value.orEmpty().map {
                SerializableClassesItem(it.id, it.name, it.studyTime, it.color)
            }

            val json: String = gson.toJson(serializableList)
            prefs.edit().putString("classes_list", json).apply()
        }

        fun saveEvent() {
            val gson = Gson()

            // create a serializable list from classeslist
            val events = Event.getEvents()

            val serializableEvents = events.orEmpty().map {
                SerializableEvent(it.name, it.date.toString(), it.time.toString(), it.classesItemId, it.repeated)
            }

            val json: String = gson.toJson(serializableEvents)
            prefs.edit().putString("events_list", json).apply()
        }

        // load from sharedPreferences
        fun load() {
            loadClasses()
            loadEvents()
        }

        fun loadClasses() {
            val gson = Gson()
            val json = prefs.getString("classes_list", null)

            if (json != null) {
                // load list of serializableClass
                val type = object : TypeToken<List<SerializableClassesItem>>() {}.type
                val list: List<SerializableClassesItem> = gson.fromJson(json, type)

                // create list of ClassesItem from this
                val restored = list.map {
                    ClassesItem(it.id, it.name, it.studyTime, it.color)
                }
                
                _classesList.value = restored
                nextId = (list.maxOfOrNull { it.id } ?: 0) + 1
            }
        }

        fun loadEvents() {
            val gson = Gson()
            val json = prefs.getString("events_list", null)

            if (json != null) {
                // load list of serializableClass
                val type = object : TypeToken<List<SerializableEvent>>() {}.type
                val list: List<SerializableEvent> = gson.fromJson(json, type)

                // create list of ClassesItem from this
                for (e in list) {
                    Event.addEvent(Event(e.name, LocalDate.parse(e.date), LocalTime.parse(e.time), e.classesItemId, e.repeated))
                }
            }
        }

        fun setClassList(newClassList : List<ClassesItem>) {
            _classesList.value = newClassList
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