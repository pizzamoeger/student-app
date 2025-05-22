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
            prefs.edit().clear().apply()
            load(context)
        }

        // load from sharedPreferences
        fun load(context: Context) {
            ClassesItem.load()
            Event.load(context)
            Assignment.load()
            Semester.load()
        }
    }
}