package com.hannah.studentapp.ui.type

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hannah.studentapp.SharedData
import com.hannah.studentapp.SharedData.Companion.prefs
import com.hannah.studentapp.ui.classesItem.ClassesItem
import com.hannah.studentapp.ui.event.Event.Companion.getJson
import com.hannah.studentapp.ui.event.SerializableEvent
import com.hannah.studentapp.ui.semester.Semester
import com.hannah.studentapp.ui.semester.Semester.Companion
import com.hannah.studentapp.ui.semester.SerializableSemester
import java.time.LocalDate

data class SerializableType(
    var neededEcts : Int,
    var classesOfType : MutableList<Int>,
    var name : String,
    var id: Int
)

class Type(
    private var neededEcts : Int = 5,
    private var classesOfType : MutableList<Int> = mutableListOf(),
    private var name : String = "Type",
    private var id: Int = nextId++
) {
    override fun toString(): String {
        return getName()
    }
    fun getName() = name
    fun setName(newName : String) {
        name = newName
        save()
    }

    fun getID() = id

    fun setECTSNeeded(newECTSNeeded : Int) {
        neededEcts = newECTSNeeded
        save()
    }
    fun getECTSNeeded() = neededEcts

    fun containsClass(ID : Int) : Boolean {
        return classesOfType.contains(ID)
    }

    fun addClass(id : Int) {
        classesOfType.add(id)
        save()
    }
    fun removeClass(id: Int) {
        classesOfType = classesOfType.filterNot { it == id }.toMutableList()
        save()
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
        private var nextId = 1
        private val typeList : MutableList<Type> = mutableListOf()

        fun addType(type: Type) {
            typeList.add(type)
            save()
        }
        fun removeType(ID : Int) {
            typeList.filterNot { it.id == ID }.toMutableList()
        }

        fun getList() = typeList

        fun get(ID: Int) : Type {
            return typeList.first { it.id == ID }
        }

        private fun getJSON() : String {
            val gson = Gson()

            val serializableTypes = typeList.map {
                SerializableType(it.neededEcts, it.classesOfType, it.name, it.id)
            }

            return gson.toJson(serializableTypes)
        }

        private fun save() {
            prefs.edit().putString("type_list", getJSON()).apply()
            SharedData.save()
        }

        fun load(jsonArg: String?) {
            val gson = Gson()
            var json = jsonArg
            if (json == null) json = prefs.getString("type_list", null)
            typeList.clear()

            if (json != null) {
                // load list of serializableClass
                val type = object : TypeToken<List<SerializableType>>() {}.type
                val list: List<SerializableType> = gson.fromJson(json, type)

                // create list of ClassesItem from this
                for (s in list) {
                    typeList.add(Type(id=s.id, name=s.name, classesOfType = s.classesOfType, neededEcts = s.neededEcts))
                }

                nextId = ((list.maxOfOrNull { it.id } ?: 0) + 1)
            }
            save()
        }
    }
}