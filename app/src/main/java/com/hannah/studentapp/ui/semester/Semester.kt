package com.hannah.studentapp.ui.semester

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hannah.studentapp.SharedData
import com.hannah.studentapp.SharedData.Companion.prefs
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hannah.studentapp.ui.type.Type
import java.time.LocalDate

data class SerializableSemester (
    var start : String,
    var end : String,
    var classesInSemester : MutableList<Int>,
    var name : String,
    var id : Int)

class Semester (
    private var start : LocalDate = LocalDate.now(),
    private var end : LocalDate = LocalDate.now().plusMonths(6),
    private var classesInSemester : MutableList<Int> = mutableListOf(0),
    private var name : String = "Semester $nextId",
    private var id : Int = nextId++) {
    override fun toString(): String {
        return name
    }
    fun setName(newName : String) {
        name=newName
    }

    fun getClasses() = classesInSemester
    fun addClass(id: Int) {
        classesInSemester.add(id)
        save()
    }
    fun removeClass(id: Int) {
        classesInSemester.removeIf{it == id}
        save()
    }

    fun getId() = id
    fun getStart() = start
    fun getEnd() = end

    companion object {
        private var semesterList : MutableList<Semester> = mutableListOf()
        private var current = Semester()
        private var nextId = 0
        fun getList() = semesterList

        fun add(semester: Semester) {
            semesterList.add(semester)
            save()
        }

        fun add(from : LocalDate = LocalDate.now(), to : LocalDate = LocalDate.now().plusMonths(6), name : String = "Semester $nextId") {
            semesterList.add(Semester(from, to, name=name))
            save()
        }

        fun delete(id : Int) {
            semesterList.removeIf{it.getId() == id}
            save()
        }

        fun getCurrent() = current
        fun setCurrent(newC : Semester) {
            current = newC
        }

        fun getJson() : String {
            val gson = Gson()

            val serializableSemester = semesterList.map {
                SerializableSemester(it.start.toString(), it.end.toString(), it.classesInSemester, it.name, it.id)
            }

            return gson.toJson(serializableSemester)
        }

        private fun saveToDB() {
            val currentUser = FirebaseAuth.getInstance().currentUser
            val db = FirebaseFirestore.getInstance()
            if (currentUser != null) {
                val userId = currentUser.uid
                val userDocRef = db.collection("user").document(userId)

                // Create a new Map for user data (or use a data class/object)
                userDocRef.update("semester", Type.getJson())
                    .addOnSuccessListener {
                        Log.d("Firestore", "Field 'types' successfully updated for user: $userId")
                    }
                    .addOnFailureListener { e ->
                        Log.w("Firestore", "Error updating 'types' field", e)
                    }

            }
        }

        private fun save() {
            prefs.edit().putString("semester_list", getJson()).apply()
            //SharedData.save()
            saveToDB()
        }

        fun load(jsonArg: String?) {
            val gson = Gson()
            var json = jsonArg
            if (json == null) json = prefs.getString("semester_list", null)
            semesterList.clear()

            if (json != null) {
                // load list of serializableClass
                val type = object : TypeToken<List<SerializableSemester>>() {}.type
                val list: List<SerializableSemester> = gson.fromJson(json, type)

                // create list of ClassesItem from this
                for (s in list) {
                    semesterList.add(Semester(id=s.id, name=s.name, start=LocalDate.parse(s.start), end=LocalDate.parse(s.end), classesInSemester = s.classesInSemester))
                }

                if (semesterList.size == 0) {
                    semesterList.add(Semester())
                }
                current = semesterList[0] // TODO temp

                nextId = ((list.maxOfOrNull { it.id } ?: 0) + 1)
            } else {
                semesterList.add(Semester())
                current = semesterList[0]
            }
            save()
        }
    }

}