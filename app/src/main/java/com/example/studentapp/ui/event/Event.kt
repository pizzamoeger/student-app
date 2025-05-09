package com.example.studentapp.ui.event

import java.time.LocalDate
import java.time.LocalTime

class Event (
    var name : String,
    var date : LocalDate,
    var time : LocalTime
) {

    companion object {
        // all events
        var eventsList : MutableList<Event> = mutableListOf()

        // get all events at date and time
        fun eventsForDateAndTime(selectedDate: LocalDate, selectedTime: LocalTime): List<Event> {
            var events : MutableList<Event> = mutableListOf<Event>()

            for (event in eventsList) {
                if (event.date == selectedDate && event.time.hour == selectedTime.hour) events.add(event)
            }

            return events
        }
    }
}