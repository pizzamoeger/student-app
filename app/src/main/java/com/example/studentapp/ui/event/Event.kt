package com.example.studentapp.ui.event

import android.util.Log
import com.example.studentapp.SharedData
import com.example.studentapp.ui.classesItem.ClassesItem
import com.example.studentapp.ui.classesItem.SerializableClassesItem
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters

// data class for ClassesItem so that gson can save
data class SerializableEvent(
    var name : String,
    var date : String,
    var time : String,
    var classesItemId : Int,
    var repeated : Boolean
)

class Event (
    var name : String = "",
    var date : LocalDate = LocalDate.now(),
    var time : LocalTime = LocalTime.now(),
    var classesItemId : Int = -1,
    var repeated : Boolean = false // todo make this yearly/weekly/biweekly/...
) {

    companion object {
        // all events
        /*private*/ var eventsList : MutableList<Event> = mutableListOf()
        /*private*/ var repeatedEventsList : MutableList<Event> = mutableListOf()

        fun addEvent(event: Event) {
            if (event.repeated) repeatedEventsList.add(event)
            else eventsList.add(event)
        }

        fun removeAllOfClass(id : Int) {
            eventsList = eventsList.filterNot { it.classesItemId == id }.toMutableList()
            repeatedEventsList = repeatedEventsList.filterNot { it.classesItemId == id }.toMutableList()
        }

        // get all events (repeated and non repeated)
        fun getEvents() : List<Event> {
            val events : MutableList<Event> = mutableListOf()
            for (event in eventsList) events.add(event)
            for (event in repeatedEventsList) events.add(event)
            return events
        }

        // get all events at date and time
        fun eventsForDateAndTime(selectedDate: LocalDate, selectedTime: LocalTime): Map<String,List<Event>> {
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
    }
}