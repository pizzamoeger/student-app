package com.example.studentapp.ui.event

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import com.example.studentapp.R
import com.example.studentapp.SharedData
import com.example.studentapp.SharedData.Companion.prefs
import com.example.studentapp.ui.semester.Semester
import com.example.studentapp.ui.timetable.TimetableWidget
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
    var repeated : Boolean
)

class Event (
    private var id : Int = nextId++,
    private var name : String = "",
    private var date : LocalDate = LocalDate.now(),
    private var time : LocalTime = LocalTime.now(),
    private var classesItemId : Int = -1,
    private var repeated : Boolean = false // todo make this yearly/weekly/biweekly/...
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

        private fun save(context: Context) {
            refreshTimetableWidget(context)
            val gson = Gson()

            val serializableEvents = eventsList.map {
                SerializableEvent(it.id, it.name, it.date.toString(), it.time.toString(), it.classesItemId, it.repeated)
            }

            val json: String = gson.toJson(serializableEvents)
            prefs.edit().putString("events_list", json).apply()
        }

        fun load(context: Context) {
            val gson = Gson()
            val json = prefs.getString("events_list", null)

            if (json != null) {
                // load list of serializableClass
                val type = object : TypeToken<List<SerializableEvent>>() {}.type
                val list: List<SerializableEvent> = gson.fromJson(json, type)

                // create list of ClassesItem from this
                for (e in list) {
                    addEvent(Event(id=e.id, name=e.name, date=LocalDate.parse(e.date), time=LocalTime.parse(e.time), classesItemId = e.classesItemId, repeated = e.repeated), context)
                }

                nextId = ((list.maxOfOrNull { it.id } ?: 0) + 1)
            }
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