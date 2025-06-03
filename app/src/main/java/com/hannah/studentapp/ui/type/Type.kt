package com.hannah.studentapp.ui.type

import com.hannah.studentapp.ui.classesItem.ClassesItem

class Type(
    private var neededEcts : Int,
    private var classesOfType : MutableList<Int>,
    private var name : String
) {
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

    fun getCompletedECTS() {
        var completedECTS = 0
        for (classID in classesOfType) {
            if (ClassesItem.get(classID).isPassed()) completedECTS += ClassesItem.get(classID).getECTS()
        }
    }
}