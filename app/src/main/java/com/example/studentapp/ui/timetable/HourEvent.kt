package com.example.studentapp.ui.timetable

import com.example.studentapp.ui.event.Event
import java.time.LocalTime

class HourEvent (var time : LocalTime,
    var events : Map<String, List<Event>>) {
}