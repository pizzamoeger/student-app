package com.example.studentapp.ui.timetable

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.studentapp.ui.calendar.CalendarUtils
import com.example.studentapp.ui.event.Event
import java.time.LocalTime

// TODO da checki iwie alles ned so ganz bruh

class HourAdapter (
    context : Context,
    hourEvents : List<HourEvent>
): ArrayAdapter<HourEvent?>(context, 0, hourEvents) {

    override fun getView(position: Int, convertViewArg: View?, parent: ViewGroup): View {
        // TODO wtf macht das do bro
        val event = getItem(position)

        var convertView = convertViewArg
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(com.example.studentapp.R.layout.hour_cell, parent, false)
        }

        setHour(convertView!!, event!!.time)
        setEventsWeek(convertView, event.events)

        return convertView
    }

    // set text for hour
    private fun setHour(convertView : View, time : LocalTime) {
        val timeTextView = convertView.findViewById<TextView>(com.example.studentapp.R.id.hour_cell)
        timeTextView.text = CalendarUtils.formattedShortTime(time)
    }

    // set events
    // TODO make this actually display the events of the day of the week
    private fun setEventsWeek(convertView : View, events : Map<String, List<Event>>) {
        val monTextView = convertView.findViewById<TextView>(com.example.studentapp.R.id.event_mon_1)
        val tueTextView = convertView.findViewById<TextView>(com.example.studentapp.R.id.event_tue_1)
        val wedTextView = convertView.findViewById<TextView>(com.example.studentapp.R.id.event_wed_1)
        val thurTextView = convertView.findViewById<TextView>(com.example.studentapp.R.id.event_thur_1)
        val friTextView = convertView.findViewById<TextView>(com.example.studentapp.R.id.event_fri_1)

        setEvents(monTextView, events["mon"]!!)
        setEvents(tueTextView, events["tue"]!!)
        setEvents(wedTextView, events["wed"]!!)
        setEvents(wedTextView, events["thur"]!!)
        setEvents(wedTextView, events["fri"]!!)
    }

    private fun setEvents(eventTextView : TextView, events : List<Event>) {
        /*if (events.isEmpty()) eventTextView.visibility = View.INVISIBLE
        else*/ eventTextView.visibility = View.VISIBLE

        if (events.size > 1) eventTextView.text = events[0].name
        // else
    }
}