package com.hannah.studentapp.ui.event

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hannah.studentapp.R
import com.hannah.studentapp.SharedData
import com.hannah.studentapp.SharedData.Companion.prefs
import com.hannah.studentapp.ui.semester.Semester
import com.hannah.studentapp.ui.timetable.TimetableWidget
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hannah.studentapp.ui.type.Type
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters

// data class for ClassesItem so that gson can save
data class SerializableEvent(
    var id : Int,
    var name : String,
    var date : String,
    var time : String,
    var classesItemId : Int,
    var repeated : Boolean,
    var room : String? = ""
)

class Event (
    private var id : Int = nextId++,
    private var name : String = "New Event",
    private var date : LocalDate = LocalDate.now(),
    private var time : LocalTime = LocalTime.now().withNano(0),
    private var classesItemId : Int = -1,
    private var repeated : Boolean = false, // todo make this yearly/weekly/biweekly/...
    private var room: String = ""
) {

    fun isRepeated() = repeated
    fun setRepeated(newR : Boolean, context: Context) {
        repeated = newR
        save(context)
    }

    fun getName() = name
    fun setName(newN : String, context: Context) {
        name= newN
        save(context)
    }

    fun getClassId() = classesItemId
    fun setClassId(newI : Int, context: Context) {
        classesItemId = newI
        save(context)
    }

    fun getId() = id

    fun getRoom() = room
    fun setRoom(newRoom : String) {
        room = newRoom
    }

    fun setDate(newD : LocalDate, context: Context) {
        date = newD
        save(context)
    }
    fun getDate() = date

    fun setTime(newT : LocalTime, context: Context) {
        time = newT
        save(context)
    }
    fun getTime() = time

    companion object {
        // all events
        private var eventsList : MutableList<Event> = mutableListOf()
        private var nextId = 0

        fun addEvent(event: Event, context: Context) {
            if (event.repeated) {
                var ev = event
                ev.repeated = false
                while (ev.date < Semester.getCurrent().getEnd()) {
                    eventsList.add(ev)
                    ev = Event(ev.id, ev.name, ev.date.plusWeeks(1), ev.time, ev.classesItemId, ev.repeated)
                }
            }
            else eventsList.add(event)
            save(context)
        }

        private fun refreshTimetableWidget(context: Context) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val ids = appWidgetManager.getAppWidgetIds(ComponentName(context, TimetableWidget::class.java))
            appWidgetManager.notifyAppWidgetViewDataChanged(ids, R.id.events_recycler_view_widget)
        }


        fun get(id : Int) : Event? {
            for (event in getEvents()) {
                if (event.id == id ) return event
            }
            return null
        }

        fun delete(id : Int, context: Context) {
            eventsList = eventsList.filterNot { it.id == id }.toMutableList()
            save(context)
        }

        fun removeAllOfClass(id : Int, context: Context) {
            eventsList = eventsList.filterNot { it.classesItemId == id }.toMutableList()
            save(context)
        }

        // get all events (repeated and non repeated)
        fun getEvents() : List<Event> {
            val events : MutableList<Event> = mutableListOf()
            for (event in eventsList.filter { Semester.getCurrent().getClasses().contains(it.classesItemId) }) events.add(event)
            return events
        }

        fun getJson() : String {
            val gson = Gson()

            val serializableEvents = eventsList.map {
                SerializableEvent(it.id, it.name, it.date.toString(), it.time.toString(), it.classesItemId, it.repeated, it.room)
            }

            return gson.toJson(serializableEvents)
        }

        private fun save(context: Context) {
            refreshTimetableWidget(context)

            prefs.edit().putString("events_list", getJson()).apply()
            //SharedData.save()
            saveToDB()
        }

        private fun saveToDB() {
            val currentUser = FirebaseAuth.getInstance().currentUser
            val db = FirebaseFirestore.getInstance()
            if (currentUser != null) {
                val userId = currentUser.uid
                val userDocRef = db.collection("user").document(userId)

                // Create a new Map for user data (or use a data class/object)
                userDocRef.update("events", Type.getJson())
                    .addOnSuccessListener {
                        Log.d("Firestore", "Field 'types' successfully updated for user: $userId")
                    }
                    .addOnFailureListener { e ->
                        Log.w("Firestore", "Error updating 'types' field", e)
                    }

            }
        }

        fun load(jsonArg: String?, context: Context) {
            val gson = Gson()
            var json = jsonArg
            if (json == null) json = prefs.getString("events_list", null)
            eventsList.clear()

            if (json != null) {
                // load list of serializableClass
                val type = object : TypeToken<List<SerializableEvent>>() {}.type
                val list: List<SerializableEvent> = gson.fromJson(json, type)

                // create list of ClassesItem from this
                for (e in list) {
                    addEvent(Event(id=e.id, name=e.name, date=LocalDate.parse(e.date), time=LocalTime.parse(e.time), classesItemId = e.classesItemId, repeated = e.repeated, room = e.room ?: ""), context)
                }

                nextId = ((list.maxOfOrNull { it.id } ?: 0) + 1)
            }
            save(context)
        }

        // get all events at date and time
        fun eventsForDateAndTimeWeek(selectedDate: LocalDate, selectedTime: LocalTime): Map<String,List<Event>> {
            val events : MutableMap<String,MutableList<Event>> = mutableMapOf()

            val days = listOf("mon", "tue", "wed", "thur", "fri")
            var dateCounter = selectedDate.with(
                TemporalAdjusters.previousOrSame(
                    DayOfWeek.MONDAY))

            for (day in days) {
                events[day] = mutableListOf()
                for (event in getEvents()) {
                    if (event.isRepeated()) {
                        if (event.date.dayOfWeek == dateCounter.dayOfWeek && event.time.hour == selectedTime.hour) {
                            events[day]!!.add(event)
                        }
                    } else {
                        if (event.date == dateCounter && event.time.hour == selectedTime.hour) {
                            events[day]!!.add(event)
                        }
                    }
                }
                // next (week) day
                dateCounter = dateCounter.plusDays(1)
            }

            return events
        }

        fun eventsForDateAndTimeDay(selectedDate: LocalDate, selectedTime: LocalTime): List<Event> {
            val events : MutableList<Event> = mutableListOf()

            for (event in getEvents()) {
                // if date and time matches for events that are not repeated
                if (event.date == selectedDate && event.time.hour == selectedTime.hour) {
                    events.add(event)
                }

            }

            return events
        }
    }
}