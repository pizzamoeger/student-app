package com.example.studentapp.ui.event

import java.time.LocalDate
import java.time.LocalTime

class Event (
    var name : String,
    var date : LocalDate,
    var time : LocalTime
) {

    companion object {
        var eventsList : MutableList<Event> = mutableListOf<Event>()
        fun eventsForDateAndTime(selectedDate: LocalDate, selectedTime: LocalTime?): List<Event> {
            var events : MutableList<Event> = mutableListOf<Event>()

            for (event in eventsList) {
                if (event.date == selectedDate) events.add(event)
            }

            return events
        }
    }
}