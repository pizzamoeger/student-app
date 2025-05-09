package com.example.studentapp.ui.timetable

import android.content.Context
import android.util.Log
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
        val monTextView1 = convertView.findViewById<TextView>(com.example.studentapp.R.id.event_mon_1)
        val tueTextView1 = convertView.findViewById<TextView>(com.example.studentapp.R.id.event_tue_1)
        val wedTextView1 = convertView.findViewById<TextView>(com.example.studentapp.R.id.event_wed_1)
        val thurTextView1 = convertView.findViewById<TextView>(com.example.studentapp.R.id.event_thur_1)
        val friTextView1 = convertView.findViewById<TextView>(com.example.studentapp.R.id.event_fri_1)

        val monTextView2 = convertView.findViewById<TextView>(com.example.studentapp.R.id.event_mon_2)
        val tueTextView2 = convertView.findViewById<TextView>(com.example.studentapp.R.id.event_tue_2)
        val wedTextView2 = convertView.findViewById<TextView>(com.example.studentapp.R.id.event_wed_2)
        val thurTextView2 = convertView.findViewById<TextView>(com.example.studentapp.R.id.event_thur_2)
        val friTextView2 = convertView.findViewById<TextView>(com.example.studentapp.R.id.event_fri_2)

        setEvents(monTextView1, monTextView2, events["mon"]!!)
        setEvents(tueTextView1, tueTextView2, events["tue"]!!)
        setEvents(wedTextView1, wedTextView2, events["wed"]!!)
        setEvents(thurTextView1, thurTextView2, events["thur"]!!)
        setEvents(friTextView1, friTextView2, events["fri"]!!)
    }

    private fun setEvents(eventTextView1 : TextView, eventTextView2 : TextView, events : List<Event>) {
        if (events.isEmpty()) {
            eventTextView1.visibility = View.INVISIBLE
            eventTextView2.visibility = View.INVISIBLE
        } else if (events.size == 1) {
            eventTextView1.visibility = View.VISIBLE
            eventTextView1.text = events[0].name
            eventTextView2.width=100 // TODO figure out how to do this
            eventTextView2.visibility = View.INVISIBLE
        } else if (events.size == 2) {
            eventTextView1.visibility = View.VISIBLE
            eventTextView1.text = events[0].name
            eventTextView2.visibility = View.VISIBLE
            eventTextView2.text = events[1].name
        } else {
            eventTextView1.visibility = View.VISIBLE
            eventTextView1.text = events[0].name
            eventTextView2.visibility = View.VISIBLE
            eventTextView2.text = "+"+(events.size-1)
        }
        // else
    }
}