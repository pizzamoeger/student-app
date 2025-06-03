package com.hannah.studentapp.ui.type

import com.hannah.studentapp.ui.classesItem.ClassesItem
import com.hannah.studentapp.ui.semester.Semester

class Type(
    private var neededEcts : Int,
    private var classesOfType : MutableList<Int> = mutableListOf(),
    private var name : String,
    private var id: Int = nextId++
) {
    fun getName() = name
    fun setName(newName : String) {
        name = newName
    }

    fun getID() = id

    fun setECTSNeeded(newECTSNeeded : Int) {
        neededEcts = newECTSNeeded
    }
    fun getECTSNeeded() = neededEcts

    fun addClass(id : Int) {
        classesOfType.add(id)
    }
    fun removeClass(id: Int) {
        classesOfType = classesOfType.filterNot { it == id }.toMutableList()
    }

    fun getCompletedECTS() : Int {
        var completedECTS = 0
        for (classID in classesOfType) {
            val clazz = ClassesItem.get(classID)
            if (clazz.isPassed()) completedECTS += clazz.getECTS()
        }
        return completedECTS
    }

    fun getOngoingECTS() : Int {
        var ongoingECTS = 0
        for (classID in classesOfType) {
            val clazz = ClassesItem.get(classID)
            if (!clazz.isPassed() && Semester.getCurrent().getClasses().contains(clazz.getId())) ongoingECTS += clazz.getECTS()
        }
        return ongoingECTS
    }

    companion object {
        private var nextId = 0
        private val typeList : MutableList<Type> = mutableListOf()

        fun addType(type: Type) {
            typeList.add(type)
        }
        fun removeType(ID : Int) {
            typeList.filterNot { it.id == ID }.toMutableList()
        }

        fun get(ID: Int) : Type {
            return typeList.first { it.id == ID }
        }
    }
}