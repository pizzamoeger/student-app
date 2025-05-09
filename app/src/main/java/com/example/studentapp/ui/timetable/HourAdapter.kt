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


class HourAdapter (
    context : Context,
    private val hourEvents : List<HourEvent>
): ArrayAdapter<HourEvent?>(context, 0, hourEvents) {

    override fun getView(position: Int, convertViewArg: View?, parent: ViewGroup): View {
        val event = getItem(position)
        var convertView = convertViewArg
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(com.example.studentapp.R.layout.hour_cell, parent, false)
        }

        setHour(convertView!!, event!!.time)
        setEvents(convertView, event.events)
        return convertView
    }

    fun setHour(convertView : View, time : LocalTime) {
        val timeTextView = convertView.findViewById<TextView>(com.example.studentapp.R.id.hour_cell)
        timeTextView.text = CalendarUtils.formattedShortTime(time)
    }

    fun setEvents(convertView : View, events : List<Event>) {
        val monTextView = convertView.findViewById<TextView>(com.example.studentapp.R.id.event_mon)
        val tueTextView = convertView.findViewById<TextView>(com.example.studentapp.R.id.event_tue)
        val wedTextView = convertView.findViewById<TextView>(com.example.studentapp.R.id.event_wed)
        val thurTextView = convertView.findViewById<TextView>(com.example.studentapp.R.id.event_thur)
        val friTextView = convertView.findViewById<TextView>(com.example.studentapp.R.id.event_fri)
        // TODO make this to week stuff
        friTextView.visibility = View.INVISIBLE
        thurTextView.visibility = View.INVISIBLE
        wedTextView.visibility = View.INVISIBLE

        if (events.size == 0) {
            monTextView.visibility = View.INVISIBLE
            tueTextView.visibility = View.INVISIBLE
            wedTextView.visibility = View.INVISIBLE
        } else if (events.size == 1) {
            setEvent(monTextView, events.get(0))
            tueTextView.visibility = View.INVISIBLE
            wedTextView.visibility = View.INVISIBLE
        } else if (events.size == 2) {
            setEvent(monTextView, events.get(0))
            setEvent(tueTextView, events.get(1))
            wedTextView.visibility = View.INVISIBLE
        } else {
            setEvent(monTextView, events.get(0))
            setEvent(tueTextView, events.get(1))
            setEvent(wedTextView, events.get(2))
        }
    }

    fun setEvent(eventTextView : TextView, event : Event) {
        eventTextView.text = event.name
        eventTextView.visibility = View.VISIBLE
    }
}