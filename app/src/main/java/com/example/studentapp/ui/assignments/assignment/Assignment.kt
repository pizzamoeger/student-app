package com.example.studentapp.ui.assignments.assignment

import com.example.studentapp.ui.classesItem.ClassesItem
import java.time.LocalDate

class Assignment (
    private var dueDate : LocalDate,
    private var classId : Int,
    private var title : String
) {
    private var completed = false
    private var progress = 0.0

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
}