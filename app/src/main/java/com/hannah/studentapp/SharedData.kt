package com.hannah.studentapp

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.hannah.studentapp.ui.assignments.assignment.Assignment
import com.hannah.studentapp.ui.classesItem.ClassesItem
import com.hannah.studentapp.ui.event.Event
import com.hannah.studentapp.ui.semester.Semester
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hannah.studentapp.ui.type.Type

enum class TimeInterval {
    DAY, WEEK, MONTH, TOTAL, DEFAULT
}

class SharedData  {
    companion object {

        lateinit var prefs: SharedPreferences

        var locked = false

        // is called once when app is created
        fun init(context: Context) {
            prefs = context.getSharedPreferences("shared_data_prefs", Context.MODE_PRIVATE)
            //prefs.edit().clear().apply()
            load(context)
        }

        fun save() {
            val currentUser = FirebaseAuth.getInstance().currentUser
            val db = FirebaseFirestore.getInstance()
            if (currentUser != null) {
                val userId = currentUser.uid

                // Create a new Map for user data (or use a data class/object)
                val userData = hashMapOf(
                    "classes" to ClassesItem.getJson(),
                    "events" to Event.getJson(),
                    "assignments" to Assignment.getJson(),
                    "semester" to Semester.getJson(),
                    "types" to Type.getJson(),
                    "username" to ""
                )

                // Get a reference to the user's document using their UID
                val userDocRef = db.collection("user").document(userId)

                // Set the data for this document
                userDocRef.set(userData)
                    .addOnSuccessListener {
                        Log.d("Firestore", "User data successfully written for user: $userId")
                    }
                    .addOnFailureListener { e ->
                        Log.w("Firestore", "Error writing user data", e)
                    }

            }
        }

        // load from sharedPreferences
        fun load(context: Context) {
            val currentUser = FirebaseAuth.getInstance().currentUser
            val db = FirebaseFirestore.getInstance()
            Type.load(null)
            if (currentUser != null) {
                val userId = currentUser.uid

                // Get a reference to the user's document
                val userDocRef = db.collection("user").document(userId)

                // Read the document
                userDocRef.get()
                    .addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            // Document exists, get the data
                            val classes = documentSnapshot.getString("classes")
                            val events = documentSnapshot.getString("events")
                            val assignments = documentSnapshot.getString("assignments")
                            val semester = documentSnapshot.getString("semester")
                            val types = documentSnapshot.getString("types")
                            val username = documentSnapshot.getString("username")
                            ClassesItem.load(classes, context)
                            Event.load(events, context)
                            Assignment.load(assignments, context)
                            Semester.load(semester)
                            Type.load(types)
                        }
                    }
            } else {
                ClassesItem.load(null, context)
                Event.load(null, context)
                Assignment.load(null, context)
                Semester.load(null)
                Type.load(null)
            }
        }
    }
}