package com.hannah.studentapp.ui.timetable

import com.hannah.studentapp.ui.event.Event
import java.time.LocalTime

class HourEvent (var time : LocalTime,
    var events : Map<String, List<Event>>) {
}