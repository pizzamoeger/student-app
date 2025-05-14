package com.example.studentapp.ui.assignments.assignment

import com.example.studentapp.SharedData.Companion.prefs
import com.example.studentapp.ui.assignments.assignment.Assignment.Companion.nextId
import com.example.studentapp.ui.classesItem.ClassesItem
import com.example.studentapp.ui.event.Event
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate

data class SerializableAssignment(
    var dueDate : String,
    var classId : Int,
    var title : String,
    var id : Int,
    var completed: Boolean,
    var progress: Double)

class Assignment (
    private var dueDate : LocalDate,
    private var classId : Int,
    private var title : String,
    private var id : Int = nextId++,
    private var completed: Boolean = false,
    private var progress: Double = 0.0
) {


    fun getDueDate() = dueDate
    fun setDueDate(due : LocalDate) {
        dueDate = due
    }

    fun getClass() : ClassesItem {
        return ClassesItem.get(classId)
    }
    fun setClass(id : Int) {
        classId = id
    }

    fun isCompleted() = completed
    fun setCompleted(c : Boolean = true) {
        completed = c
        progress = if (c) 1.0 else 0.0
    }

    fun getProgress() = progress
    fun setProgress(p : Double) {
        progress = p
        if (p == 1.0) setCompleted()
    }

    fun getTitle() = title
    fun setTitle(newTitle : String) {
        title = newTitle
    }

    companion object {
        private var assignmentsList : MutableList<Assignment> = mutableListOf()
        private var nextId = 0

        fun add(assignment: Assignment) {
            assignmentsList.add(assignment)
            assignmentsList.sortBy { it.dueDate }
            save()
        }

        fun delete(id : Int) {
            assignmentsList.removeIf{it.id == id}
        }

        fun getByIndex(index : Int) = assignmentsList[index]
        fun get(id : Int) = assignmentsList.find{it.id == id}

        fun getList() = assignmentsList

        fun save() {
            val gson = Gson()

            // create a serializable list from classeslist
            val serializableList = assignmentsList.map {
                SerializableAssignment(it.dueDate.toString(), it.classId, it.title, it.id, it.completed, it.progress)
            }

            val json: String = gson.toJson(serializableList)
            prefs.edit().putString("assignments_list", json).apply()
        }

        fun load() {
            val gson = Gson()
            val json = prefs.getString("assignments_list", null)

            if (json != null) {
                // load list of serializableClass
                val type = object : TypeToken<List<SerializableAssignment>>() {}.type
                val list: List<SerializableAssignment> = gson.fromJson(json, type)

                // create list of ClassesItem from this
                val restored = list.map {
                    Assignment(LocalDate.parse(it.dueDate), it.classId, it.title, it.id, it.completed, it.progress)
                }

                assignmentsList = restored.toMutableList()
                nextId = ((list.maxOfOrNull { it.id } ?: 0) + 1)
            }
        }
    }
}