package com.example.studentapp.ui.classesItem

import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.studentapp.R
import com.example.studentapp.SharedData
import com.example.studentapp.SharedData.Companion.prefs
import com.example.studentapp.SharedData.Companion.today
import com.example.studentapp.TimeInterval
import com.example.studentapp.ui.event.Event
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.util.Locale
import kotlin.random.Random

data class ClassesItem(
    val id: Int = nextId++,
    var name: String = "",
    val studyTime: MutableMap<String, Int> = mutableMapOf(),
    var color : Int = 0 // TODO this is hacky transparent
) {
    override fun toString(): String {
        return name
    }

    // get seconds spent in timeframe
    private fun secondsInInterval(from : String, to : String) : Int {
        var seconds = 0;
        studyTime.forEach { entry ->
            if (entry.key in from..to) {
                seconds += entry.value
            }
        }
        return seconds
    }

    // returns seconds in timespan depending on type
    fun getSeconds(type : TimeInterval = TimeInterval.DEFAULT, from: String = "", to: String = "") : Int{
        if (type == TimeInterval.DAY) return secondsToday()
        if (type == TimeInterval.WEEK) return secondsThisWeek()
        if (type == TimeInterval.MONTH) return secondsThisMonth()
        if (type == TimeInterval.TOTAL) return secondsTotal()
        if (from == "" || to == "") {
            Log.e("GET SECONDS", "called ClassesItem.getSeconds with DEFAULT but no from or to")
            return 0
        }
        return secondsInInterval(from, to)
    }

    // get seconds spent studying today
    private fun secondsToday() : Int {
        SharedData.updateDate()
        val today = SharedData.today.value
        return secondsInInterval(today.toString(), today.toString())
    }

    // get seconds spent studying this week
    private fun secondsThisWeek() : Int {
        SharedData.updateDate()
        val today : LocalDate? = SharedData.today.value
        // TODO be able to choose when week starts
        val monday = today!!.with(TemporalAdjusters.previous(DayOfWeek.MONDAY))
        return secondsInInterval(monday.toString(), today.toString())
    }

    // get seconds spent studying this month
    private fun secondsThisMonth() : Int {
        SharedData.updateDate()
        val today : LocalDate? = SharedData.today.value
        val firstOfMonth = LocalDate.of(today!!.year, today.month, 1)
        return secondsInInterval(firstOfMonth.toString(), today.toString())
    }

    // get total seconds spent studying
    private fun secondsTotal() : Int {
        SharedData.updateDate()
        val today : LocalDate? = SharedData.today.value
        return secondsInInterval(LocalDate.MIN.toString(), today.toString())
    }

    // string of secondsToday
    private val _text = MutableLiveData<String>().apply {
        value = getTimeStringFromSeconds(secondsToday())
    }
    val text : LiveData<String> = _text

    // if this class is currently being tracked
    private val _tracking = MutableLiveData<Boolean>().apply {
        value = false
    }
    val tracking : LiveData<Boolean> = _tracking

    // update string for secondsToday
    fun updateText() {
        _text.value = getTimeStringFromSeconds(secondsToday())
    }

    // update if this class is being tracked
    fun updateTracking(to : Boolean) {
        _tracking.value = to
    }

    // reset secondsToday
    fun reset() {
        updateText()
    }

    // static
    companion object {
        private var nextId = 1;
        private var classesList : MutableList<ClassesItem> = mutableListOf() // list of all classes

        private var currentClass = ClassesItem()

        // switch currentClass to item
        fun switch(item: ClassesItem): Boolean {
            // returns true if the class changed
            if (currentClass == item) {
                item.updateTracking(!item.tracking.value!!)
                return false
            }
            currentClass.updateTracking(false)
            item.updateTracking(true)
            currentClass = item
            return true
        }

        fun getCurrent() : ClassesItem = currentClass;

        // TODO makes no sense here
        fun getTimeStringFromSeconds(secs: Int): String {
            // calculate hours, minutes and seconds and update _time accordingly
            val hours = secs / 3600
            val minutes = (secs % 3600) / 60
            val seconds = secs % 60

            val time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds)
            return time
        }

        fun getCount() : Int = classesList.size

        fun getList() : List<ClassesItem> = classesList

        // add class to classList
        fun add(name: String, color : Int) : ClassesItem {
            val newClass = ClassesItem(name=name, studyTime = mutableMapOf(), color = color)
            classesList.add(newClass)
            save()
            return newClass
        }

        // get the class with this id or default
        fun get(id : Int) : ClassesItem {
            for (item in classesList) {
                if (item.id == id) return item
            }
            Log.e("ClassesItem", "Tried to get a class with id not in classList")
            return ClassesItem()
        }

        fun getByIndex(index : Int) : ClassesItem {
            if (index >= classesList.size) {
                Log.e("ClassesItem", "Tried to get a class with index bigger than size")
                return ClassesItem()
            }
            return classesList[index]
        }

        // delete class by id from classList
        fun delete(id: Int) {
            classesList.removeAll{it.id == id}
            Event.removeAllOfClass(id)
            save()
        }

        fun save() {
            val gson = Gson()

            // create a serializable list from classeslist
            val serializableList = classesList.map {
                SerializableClassesItem(it.id, it.name, it.studyTime, it.color)
            }

            val json: String = gson.toJson(serializableList)
            prefs.edit().putString("classes_list", json).apply()
        }

        fun load() {
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

                classesList = restored.toMutableList()
                nextId = ((list.maxOfOrNull { it.id } ?: 0) + 1)
            }
        }
    }
}