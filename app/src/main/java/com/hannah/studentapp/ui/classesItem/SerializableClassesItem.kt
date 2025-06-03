package com.hannah.studentapp.ui.classesItem

import com.hannah.studentapp.ui.grades.Grade

// data class for ClassesItem so that gson can save
data class SerializableClassesItem(
    val id: Int,
    val name: String,
    val studyTime: MutableMap<String, Int>,
    val grades: MutableList<Grade>,
    val ects: Int?,
    val passed: Boolean?,
    val color : Int
)