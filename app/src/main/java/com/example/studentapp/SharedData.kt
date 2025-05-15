package com.example.studentapp

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.studentapp.ui.assignments.assignment.Assignment
import com.example.studentapp.ui.classesItem.ClassesItem
import com.example.studentapp.ui.classesItem.SerializableClassesItem
import com.example.studentapp.ui.event.Event
import com.example.studentapp.ui.event.SerializableEvent
import com.example.studentapp.ui.getThemeColor
import com.example.studentapp.ui.semester.Semester
import com.example.studentapp.ui.semester.SerializableSemester
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate
import java.time.LocalTime

enum class TimeInterval {
    DAY, WEEK, MONTH, TOTAL, DEFAULT
}

class SharedData  {
    companion object {

        lateinit var prefs: SharedPreferences

        var locked = false

        // is called once when app is created
        fun init(context: Context) {
            prefs = context.getSharedPreferences("shared_data_prefs", Context.MODE_PRIVATE)
            //prefs.edit().clear().apply()
            load()
        }

        // load from sharedPreferences
        fun load() {
            ClassesItem.load()
            Event.load()
            Assignment.load()
            //Semester.load()
        }

        fun loadSemester() {
            val gson = Gson()
            val json = prefs.getString("semester_list", null)

            if (json != null) {
                // load list of serializableClass
                val type = object : TypeToken<List<SerializableSemester>>() {}.type
                val list: List<SerializableSemester> = gson.fromJson(json, type)

                // create list of ClassesItem from this
                val restored = list.map {
                    Semester(it.start, it.end, it.classesInSemester)
                }

                Semester.semesterList = restored.toMutableList()
                // TODO                 currentClass.setNextId((list.maxOfOrNull { it.id } ?: 0) + 1)
            }
        }
    }
}