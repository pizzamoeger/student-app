package com.example.studentapp.ui.classes

// data class for ClassesItem so that gson can save
data class SerializableClassesItem(
    val id: Int,
    val name: String,
    val studyTime: MutableMap<String, Int>
)