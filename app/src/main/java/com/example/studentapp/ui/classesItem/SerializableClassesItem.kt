package com.example.studentapp.ui.classesItem

import android.graphics.Color

// data class for ClassesItem so that gson can save
data class SerializableClassesItem(
    val id: Int,
    val name: String,
    val studyTime: MutableMap<String, Int>,
    val grades: MutableList<Pair<Float,Float>>,
    val color : Int
)