package com.example.studentapp.ui.timetable

import androidx.recyclerview.widget.RecyclerView
import com.example.studentapp.databinding.HourCellBinding
import com.example.studentapp.ui.timetable.Event
import java.time.LocalTime

class HourEvent (var time : LocalTime,
    var events : List<Event>) {
}