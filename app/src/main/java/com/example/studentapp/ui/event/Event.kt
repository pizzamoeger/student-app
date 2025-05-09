package com.example.studentapp.ui.event

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters

class Event (
    var name : String,
    var date : LocalDate,
    var time : LocalTime
) {

    companion object {
        // all events
        var eventsList : MutableList<Event> = mutableListOf()

        // get all events at date and time
        fun eventsForDateAndTime(selectedDate: LocalDate, selectedTime: LocalTime): Map<String,List<Event>> {
            var events : MutableMap<String,MutableList<Event>> = mutableMapOf()

            val days = listOf("mon", "tue", "wed", "thur", "fri")
            val dateCounter = selectedDate.with(
                TemporalAdjusters.previousOrSame(
                    DayOfWeek.MONDAY))

            for (day in days) {
                events[day] = mutableListOf()
                for (event in eventsList) {
                    if (event.date == selectedDate && event.time.hour == selectedTime.hour) {
                        events[day]!!.add(event)
                    }
                }
                dateCounter.plusDays(1)
            }

            return events
        }
    }
}