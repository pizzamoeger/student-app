package com.example.studentapp.ui.classesItem

import android.graphics.Color
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.studentapp.R
import com.example.studentapp.SharedData
import com.example.studentapp.SharedData.Companion.prefs
import com.example.studentapp.TimeInterval
import com.example.studentapp.ui.assignments.assignment.Assignment
import com.example.studentapp.ui.event.Event
import com.example.studentapp.ui.grades.Grade
import com.example.studentapp.ui.semester.Semester
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.util.Locale
import kotlin.random.Random

data class ClassesItem(
    private val id: Int = nextId++,
    private var name: String = "",
    private val studyTime: MutableMap<String, Int> = mutableMapOf(),
    private val grades: MutableList<Grade> = mutableListOf(),
    private var color : Int = 0 // TODO this is hacky transparent
) {
    private var gradeId = 0
    override fun toString(): String {
        return name
    }
    fun setName(newName : String) {
        name = newName
        save()
    }

    fun getAverage() : Float {

        val weightedSum = grades.sumOf { (it.grade * it.weight).toDouble() }
        val totalWeight = grades.sumOf { it.weight.toDouble() }

        if (totalWeight == 0.0) return -1f

        return (weightedSum/totalWeight).toFloat()
    }
    fun getGrades() : List<Grade> = grades
    fun addGrade(grade : Float, weight : Float) {
        grades.add(Grade(weight, grade, gradeId, "Exam $gradeId"))
        gradeId++
        save()
    }
    fun deleteGrade(id : Int) {
        grades.removeIf { it.id == id }
        save()
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

    fun getId() = id
    fun getColor() = color
    fun setColor(newColor : Int) {
        color = newColor
        save()
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

    fun addSecond() {
        val today = LocalDate.now().toString()
        studyTime[today] = studyTime.getOrDefault(today, 0)+1
        save()
    }

    // get seconds spent studying today
    private fun secondsToday() : Int {
        val today = LocalDate.now()
        return secondsInInterval(today.toString(), today.toString())
    }

    // get seconds spent studying this week
    private fun secondsThisWeek() : Int {
        val today = LocalDate.now()
        // TODO be able to choose when week starts
        val monday = today!!.with(TemporalAdjusters.previous(DayOfWeek.MONDAY))
        return secondsInInterval(monday.toString(), today.toString())
    }

    // get seconds spent studying this month
    private fun secondsThisMonth() : Int {
        val today = LocalDate.now()
        val firstOfMonth = LocalDate.of(today!!.year, today.month, 1)
        return secondsInInterval(firstOfMonth.toString(), today.toString())
    }

    // get total seconds spent studying
    private fun secondsTotal() : Int {
        return secondsInInterval(LocalDate.MIN.toString(), LocalDate.now().toString())
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

    // static
    companion object {
        private var nextId = 1;
        private var classesList : MutableList<ClassesItem> = mutableListOf() // list of all classes

        private var currentClass = ClassesItem()

        // switch currentClass to item
        fun switch(item: ClassesItem, track:Boolean=true): Boolean {
            if (!track) {
                currentClass = item
                return true
            }
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

        fun getCurrent() : ClassesItem = currentClass

        // TODO makes no sense here
        fun getTimeStringFromSeconds(secs: Int): String {
            // calculate hours, minutes and seconds and update _time accordingly
            val hours = secs / 3600
            val minutes = (secs % 3600) / 60
            val seconds = secs % 60

            val time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds)
            return time
        }

        fun getList() : List<ClassesItem> {
            return classesList.filter { Semester.getCurrent().getClasses().contains(it.id) }
        }

        // add class to classList
        fun add(name: String, color : Int) : ClassesItem {
            val newClass = ClassesItem(name=name, studyTime = mutableMapOf(), color = color)
            classesList.add(newClass)
            Semester.getCurrent().addClass(newClass.id)
            save()
            return newClass
        }

        // get the class with this id or default
        fun get(id : Int) : ClassesItem {
            for (item in getList()) {
                if (item.id == id) return item
            }
            Log.e("ClassesItem", "Tried to get a class with id not in classList")
            return ClassesItem()
        }

        fun getByIndex(index : Int) : ClassesItem {
            if (index >= getList().size) {
                Log.e("ClassesItem", "Tried to get a class with index bigger than size")
                return ClassesItem()
            }
            return getList()[index]
        }

        // delete class by id from classList
        fun delete(id: Int) {
            classesList.removeAll{it.id == id}
            Semester.getCurrent().removeClass(id)
            Event.removeAllOfClass(id)
            Assignment.removeAllOfClass(id)
            save()
        }

         fun save() {
            val gson = Gson()

            // create a serializable list from classeslist
            val serializableList = classesList.map {
                SerializableClassesItem(it.id, it.name, it.studyTime, it.grades, it.color)
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
                    ClassesItem(it.id, it.name, it.studyTime, it.grades, it.color)
                }

                for (item in restored) {
                    item.gradeId = ((item.grades.maxOfOrNull { it.id } ?: 0) + 1)
                }

                classesList = restored.toMutableList()
                nextId = ((list.maxOfOrNull { it.id } ?: 0) + 1)
            }
        }
    }
}