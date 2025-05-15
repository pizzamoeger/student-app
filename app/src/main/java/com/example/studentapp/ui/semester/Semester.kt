package com.example.studentapp.ui.semester

import com.example.studentapp.ui.classesItem.ClassesItem
import java.time.LocalDate

data class SerializableSemester (var start : LocalDate, var end : LocalDate, var classesInSemester : MutableList<Int>)

class Semester (
    private var start : LocalDate,
    private var end : LocalDate,
    private var classesInSemester : MutableList<Int> = mutableListOf(),
    private var name : String,
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
        private var nextId = 0
        fun getList() = semesterList

        fun add(from : LocalDate = LocalDate.now(), to : LocalDate = LocalDate.now().plusMonths(6), name : String = "HS") {
            semesterList.add(Semester(from, to, name=name))
        }

        fun delete(id : Int) {
            semesterList.removeIf{it.getId() == id}
        }
    }

}