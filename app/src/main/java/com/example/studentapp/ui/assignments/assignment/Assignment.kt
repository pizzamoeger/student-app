package com.example.studentapp.ui.assignments.assignment

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.util.Log
import com.example.studentapp.R
import com.example.studentapp.SharedData.Companion.prefs
import com.example.studentapp.ui.assignments.AssignmentWidget
import com.example.studentapp.ui.classesItem.ClassesItem
import com.example.studentapp.ui.semester.Semester
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate

data class SerializableAssignment(
    var dueDate : String,
    var classId : Int,
    var title : String,
    var id : Int,
    var completed: Boolean,
    var progress: Int)

class Assignment (
    private var dueDate : LocalDate,
    private var classId : Int,
    private var title : String,
    private var id : Int = nextId++,
    private var completed: Boolean = false,
    private var progress: Int = 0
) {
    // TODO you could do subtasks
    // TODO but for now i will only do slider where you can indicate

    fun getDueDate() = dueDate
    fun setDueDate(due : LocalDate, context: Context) {
        dueDate = due
        save(context)
    }

    fun getId() = id

    fun getClass() : ClassesItem {
        return ClassesItem.get(classId)
    }
    fun setClass(id : Int, context: Context) {
        classId = id
        save(context)
    }

    fun isCompleted() = completed
    fun setCompleted(c : Boolean = true, context: Context) {
        completed = c
        progress = if (c) 100 else 0
        save(context)
    }

    fun getProgress() = progress
    fun setProgress(p : Int, context: Context) {
        progress = p
        if (p >= 100) setCompleted(context=context)
        save(context)
    }

    fun getTitle() = title
    fun setTitle(newTitle : String, context: Context) {
        title = newTitle
        save(context)
    }

    override fun toString(): String {
        return getTitle()
    }

    companion object {
        private var assignmentsList : MutableList<Assignment> = mutableListOf()
        private var nextId = 0

        fun add(assignment: Assignment, context: Context) {
            assignmentsList.add(assignment)
            assignmentsList.sortBy { it.dueDate }
            save(context)
        }

        fun delete(id : Int, context: Context) {
            assignmentsList.removeIf{it.id == id}
            save(context)
        }

        fun removeAllOfClass(id : Int, context: Context) {
            assignmentsList = assignmentsList.filterNot { it.classId == id }.toMutableList()
            save(context)
        }

        fun get(id : Int) : Assignment {
            for (item in getList()) {
                if (item.id == id) return item
            }
            Log.e("Assignment", "Tried to get an assignment with id not in classList")
            return Assignment(LocalDate.now(), -1, "")
        }

        fun getByIndex(index : Int) : Assignment {
            if (index >= getList().size) {
                Log.e("Assignment", "Tried to get an assignment with index bigger than size")
                return Assignment(LocalDate.now(), -1, "")
            }
            return getList()[index]
        }

        fun getUncompletedByIndex(index : Int) : Assignment? {
            var count = 0
            for (assignment in assignmentsList) {
                if (assignment.isCompleted()) continue
                if (!Semester.getCurrent().getClasses().contains(assignment.getClass().getId())) continue;
                if (count == index) return assignment
                count++
            }
            Log.d("AssignmentUncompletedByIndex", "There are not that many uncompleted assignmetns")
            return null
        }

        fun getList() : List<Assignment> {
            return assignmentsList.filter { Semester.getCurrent().getClasses().contains(it.classId) }
        }

        fun getUncompletedList() : List<Assignment> {
            return getList().filterNot { it.isCompleted() }
        }

        fun getUncompletedDay(day : LocalDate) : List<Assignment> {
            return getUncompletedList().filter { it.dueDate == day }
        }

        fun getListDay(day : LocalDate) : List<Assignment> {
            return getList().filter { it.dueDate == day }
        }

        fun getJson() : String {
            val gson = Gson()

            // create a serializable list from classeslist
            val serializableList = assignmentsList.map {
                SerializableAssignment(it.dueDate.toString(), it.classId, it.title, it.id, it.completed, it.progress)
            }

            return gson.toJson(serializableList)
        }

        private fun save(context : Context) {
            refreshAssignmentWidget(context)
            prefs.edit().putString("assignments_list", getJson()).apply()
        }

        fun load(jsonArg: String?, context: Context) {
            val gson = Gson()
            var json = jsonArg
            if (json == null) json = prefs.getString("assignments_list", null)
            assignmentsList.clear()

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
            save(context)
        }

        private fun refreshAssignmentWidget(context: Context) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val ids = appWidgetManager.getAppWidgetIds(ComponentName(context, AssignmentWidget::class.java))
            appWidgetManager.notifyAppWidgetViewDataChanged(ids, R.id.assignments_recycler_view_widget)
        }
    }
}