package com.example.studentapp.ui.semester

import com.example.studentapp.ui.classesItem.ClassesItem
import java.time.LocalDate

data class SerializableSemester (var start : LocalDate, var end : LocalDate, var classesInSemester : MutableList<Int>)

class Semester (var start : LocalDate, var end : LocalDate, var classesInSemester : MutableList<Int> = mutableListOf()) {
    companion object {
        var semesterList : MutableList<Semester> = mutableListOf()
    }

}