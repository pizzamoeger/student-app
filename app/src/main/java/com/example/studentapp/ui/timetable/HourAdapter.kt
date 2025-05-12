package com.example.studentapp.ui.timetable

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.example.studentapp.R
import com.example.studentapp.SharedData
import com.example.studentapp.ui.calendar.CalendarUtils
import com.example.studentapp.ui.event.Event
import com.example.studentapp.ui.getThemeColor
import java.time.LocalTime

class HourAdapter (
    context : Context,
    hourEvents : List<HourEvent>
): ArrayAdapter<HourEvent?>(context, 0, hourEvents) {

    override fun getView(position: Int, convertViewArg: View?, parent: ViewGroup): View {
        // set view
        val event = getItem(position)

        var convertView = convertViewArg
        if (convertView == null) {
            // if view is null we have to create it
            convertView = LayoutInflater.from(context).inflate(R.layout.hour_cell, parent, false)
        }

        // set the correct hour
        setHour(convertView!!, event!!.time)
        // set all events for this week
        setEventsWeek(convertView, event.events)

        return convertView
    }

    // set text for hour
    private fun setHour(convertView : View, time : LocalTime) {
        val timeTextView = convertView.findViewById<TextView>(R.id.hour_cell)
        timeTextView.text = CalendarUtils.formattedShortTime(time)
    }

    // set events
    private fun setEventsWeek(convertView : View, events : Map<String, List<Event>>) {
        // get all text views
        val monTextView1 = convertView.findViewById<TextView>(R.id.event_mon_1)
        val tueTextView1 = convertView.findViewById<TextView>(R.id.event_tue_1)
        val wedTextView1 = convertView.findViewById<TextView>(R.id.event_wed_1)
        val thurTextView1 = convertView.findViewById<TextView>(R.id.event_thur_1)
        val friTextView1 = convertView.findViewById<TextView>(R.id.event_fri_1)

        val monTextView2 = convertView.findViewById<TextView>(R.id.event_mon_2)
        val tueTextView2 = convertView.findViewById<TextView>(R.id.event_tue_2)
        val wedTextView2 = convertView.findViewById<TextView>(R.id.event_wed_2)
        val thurTextView2 = convertView.findViewById<TextView>(R.id.event_thur_2)
        val friTextView2 = convertView.findViewById<TextView>(R.id.event_fri_2)

        // set all events for mon-fri
        setEvents(monTextView1, monTextView2, events["mon"]!!)
        setEvents(tueTextView1, tueTextView2, events["tue"]!!)
        setEvents(wedTextView1, wedTextView2, events["wed"]!!)
        setEvents(thurTextView1, thurTextView2, events["thur"]!!)
        setEvents(friTextView1, friTextView2, events["fri"]!!)
    }

    private fun setEvents(eventTextView1 : TextView, eventTextView2 : TextView, events : List<Event>) {

        if (events.isEmpty()) {
            // if we have no events then both should be invisible
            setEventTextInvisible(eventTextView1)
            setEventTextInvisible(eventTextView2)
        } else if (events.size == 1) {
            // if we have one then only first should be visible
            val class1 = SharedData.classList.value!!.find { item -> item.id == events[0].classesItemId }
            setEventTextVisible(eventTextView1, events[0].name, class1!!.color)
            setEventTextInvisible(eventTextView2)
        } else if (events.size == 2) {
            // if we have two, both should be visible
            val class1 = SharedData.classList.value!!.find { item -> item.id == events[0].classesItemId }
            val class2 = SharedData.classList.value!!.find { item -> item.id == events[1].classesItemId }
            setEventTextVisible(eventTextView1, events[0].name, class1!!.color)
            setEventTextVisible(eventTextView2, events[1].name, class2!!.color)
        } else {
            // if we have more, both things should be visible but second should display how many more we have
            val class1 = SharedData.classList.value!!.find { item -> item.id == events[0].classesItemId }
            setEventTextVisible(eventTextView1, events[0].name, class1!!.color)
            setEventTextVisible(eventTextView2, "+"+(events.size-1), context.getThemeColor(R.attr.windowBackgroundMuted))
        }
    }

    private fun setEventTextVisible(eventTextView : TextView, text : String, color : Int) {
        // set the thingy to visible and make it have the correct width, text and color
        val params = eventTextView.layoutParams as LinearLayout.LayoutParams
        eventTextView.visibility = View.VISIBLE
        eventTextView.text = text
        eventTextView.setBackgroundColor(color)
        params.weight=1f
        eventTextView.layoutParams = params
    }

    private fun setEventTextInvisible(eventTextView : TextView) {
        // set thingy to invisible and width to 0
        val params = eventTextView.layoutParams as LinearLayout.LayoutParams
        eventTextView.visibility = View.INVISIBLE
        params.weight=0f
        eventTextView.layoutParams = params
    }
}