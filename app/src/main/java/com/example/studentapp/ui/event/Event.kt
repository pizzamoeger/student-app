package com.example.studentapp.ui.event

import com.example.studentapp.SharedData.Companion.prefs
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

class Event ( // TODO make all of this private
    var id : Int = nextId++,
    var name : String = "",
    var date : LocalDate = LocalDate.now(),
    var time : LocalTime = LocalTime.now(),
    var classesItemId : Int = -1,
    var repeated : Boolean = false // todo make this yearly/weekly/biweekly/...
) {

    companion object {
        // all events
        private var eventsList : MutableList<Event> = mutableListOf()
        private var repeatedEventsList : MutableList<Event> = mutableListOf()
        private var nextId = 0

        fun addEvent(event: Event) {
            if (event.repeated) repeatedEventsList.add(event)
            else eventsList.add(event)
            save()
        }

        fun get(id : Int) : Event? {
            for (event in eventsList) {
                if (event.id == id) return event
            }
            for (event in repeatedEventsList) {
                if (event.id == id) return event
            }
            return null
        }

        fun delete(id : Int) {
            eventsList = eventsList.filterNot { it.id == id }.toMutableList()
            repeatedEventsList = repeatedEventsList.filterNot { it.id == id }.toMutableList()
            save()
        }

        fun removeAllOfClass(id : Int) {
            eventsList = eventsList.filterNot { it.classesItemId == id }.toMutableList()
            repeatedEventsList = repeatedEventsList.filterNot { it.classesItemId == id }.toMutableList()
            save()
        }

        // get all events (repeated and non repeated)
        fun getEvents() : List<Event> {
            val events : MutableList<Event> = mutableListOf()
            for (event in eventsList) events.add(event)
            for (event in repeatedEventsList) events.add(event)
            return events
        }

        private fun save() {
            val gson = Gson()

            val serializableEvents = eventsList.map {
                SerializableEvent(it.id, it.name, it.date.toString(), it.time.toString(), it.classesItemId, it.repeated)
            }

            val json: String = gson.toJson(serializableEvents)
            prefs.edit().putString("events_list", json).apply()
        }

        fun load() {
            val gson = Gson()
            val json = prefs.getString("events_list", null)

            if (json != null) {
                // load list of serializableClass
                val type = object : TypeToken<List<SerializableEvent>>() {}.type
                val list: List<SerializableEvent> = gson.fromJson(json, type)

                // create list of ClassesItem from this
                for (e in list) {
                    addEvent(Event(id=e.id, name=e.name, date=LocalDate.parse(e.date), time=LocalTime.parse(e.time), classesItemId = e.classesItemId, repeated = e.repeated))
                }

                nextId = ((list.maxOfOrNull { it.id } ?: 0) + 1)
            }
        }

        // get all events at date and time
        fun eventsForDateAndTimeWeek(selectedDate: LocalDate, selectedTime: LocalTime): Map<String,List<Event>> {
            var events : MutableMap<String,MutableList<Event>> = mutableMapOf()

            val days = listOf("mon", "tue", "wed", "thur", "fri")
            var dateCounter = selectedDate.with(
                TemporalAdjusters.previousOrSame(
                    DayOfWeek.MONDAY))

            for (day in days) {
                events[day] = mutableListOf()
                for (event in eventsList) {
                    // if date and time matches for events that are not repeated
                    if (event.date == dateCounter && event.time.hour == selectedTime.hour) {
                        events[day]!!.add(event)
                    }
                }
                for (event in repeatedEventsList) {
                    // if weekday and time matches for repeated events
                    if (event.date.dayOfWeek == dateCounter.dayOfWeek && event.time.hour == selectedTime.hour) {
                        events[day]!!.add(event)
                    }
                }
                // next (week) day
                dateCounter = dateCounter.plusDays(1)
            }

            return events
        }

        fun eventsForDateAndTimeDay(selectedDate: LocalDate, selectedTime: LocalTime): List<Event> {
            var events : MutableList<Event> = mutableListOf()

            for (event in eventsList) {
                // if date and time matches for events that are not repeated
                if (event.date == selectedDate && event.time.hour == selectedTime.hour) {
                    events.add(event)
                }
            }
            for (event in repeatedEventsList) {
                // if weekday and time matches for repeated events
                if (event.date.dayOfWeek == selectedDate.dayOfWeek && event.time.hour == selectedTime.hour) {
                    events.add(event)
                }
            }

            return events
        }
    }
}