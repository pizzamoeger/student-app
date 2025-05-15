package com.example.studentapp.ui.semester

import com.example.studentapp.SharedData.Companion.prefs
import com.example.studentapp.ui.classesItem.ClassesItem
import com.example.studentapp.ui.event.Event
import com.example.studentapp.ui.event.Event.Companion.addEvent
import com.example.studentapp.ui.event.SerializableEvent
import com.example.studentapp.ui.semester.Semester.Companion.nextId
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate
import java.time.LocalTime

data class SerializableSemester (
    var start : String,
    var end : String,
    var classesInSemester : MutableList<Int>,
    var name : String,
    var id : Int)

class Semester (
    private var start : LocalDate = LocalDate.now(),
    private var end : LocalDate = LocalDate.now().plusMonths(6),
    private var classesInSemester : MutableList<Int> = mutableListOf(),
    private var name : String = "Semester $nextId",
    private var id : Int = nextId++) {
    override fun toString(): String {
        return name
    }
    fun setName(newName : String) {
        name=newName
    }

    fun getId() = id
    fun getStart() = start
    fun getEnd() = end

    companion object {
        private var semesterList : MutableList<Semester> = mutableListOf()
        private var current = Semester()
        private var nextId = 0
        fun getList() = semesterList

        fun add(from : LocalDate = LocalDate.now(), to : LocalDate = LocalDate.now().plusMonths(6), name : String = "Semester $nextId") {
            semesterList.add(Semester(from, to, name=name))
            save()
        }

        fun delete(id : Int) {
            semesterList.removeIf{it.getId() == id}
            save()
        }

        private fun save() {
            val gson = Gson()

            val serializableSemester = semesterList.map {
                SerializableSemester(it.start.toString(), it.end.toString(), it.classesInSemester, it.name, it.id)
            }

            val json: String = gson.toJson(serializableSemester)
            prefs.edit().putString("semester_list", json).apply()
        }

        fun load() {
            val gson = Gson()
            val json = prefs.getString("semester_list", null)

            if (json != null) {
                // load list of serializableClass
                val type = object : TypeToken<List<SerializableSemester>>() {}.type
                val list: List<SerializableSemester> = gson.fromJson(json, type)

                // create list of ClassesItem from this
                for (s in list) {
                    semesterList.add(Semester(id=s.id, name=s.name, start=LocalDate.parse(s.start), end=LocalDate.parse(s.end), classesInSemester = s.classesInSemester))
                }

                nextId = ((list.maxOfOrNull { it.id } ?: 0) + 1)
            }
        }
    }

}