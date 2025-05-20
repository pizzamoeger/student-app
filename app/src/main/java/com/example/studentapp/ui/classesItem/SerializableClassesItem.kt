package com.example.studentapp.ui.classesItem

import android.graphics.Color
import com.example.studentapp.ui.grades.Grade

// data class for ClassesItem so that gson can save
data class SerializableClassesItem(
    val id: Int,
    val name: String,
    val studyTime: MutableMap<String, Int>,
    val grades: MutableList<Grade>,
    val color : Int
)