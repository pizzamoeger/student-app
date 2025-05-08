package com.example.studentapp.ui.timetable

import java.time.LocalDate
import java.time.LocalTime

class Event {
    val name = "Event"

    companion object {
        fun eventsForDateAndTime(selectedDate: LocalDate, time: LocalTime?): List<Event> {
            // TODO
            return List(0, {_->Event()})
        }
    }
}