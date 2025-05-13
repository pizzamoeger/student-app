package com.example.studentapp.ui.timetable

import android.content.Context
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
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters

class DayHourAdapter (
    context : Context,
    hourEvents : List<HourEvent>,
    private val onCellEventClicked: (Int) -> Unit)
    : ArrayAdapter<HourEvent?>(context, 0, hourEvents) {

    override fun getView(position: Int, convertViewArg: View?, parent: ViewGroup): View {
        // set view
        val event = getItem(position)

        var convertView = convertViewArg
        if (convertView == null) {
            // if view is null we have to create it
            convertView = LayoutInflater.from(context).inflate(R.layout.hour_cell_day, parent, false)
        }

        // set the correct hour
        setHour(convertView!!, event!!.time)
        // set all events for this week

        setEvents(convertView, event.events[""]!!)
        // bind all cells
        // bindCells(convertView, event.time)

        return convertView
    }

    // set text for hour
    private fun setHour(convertView : View, time : LocalTime) {
        val timeTextView = convertView.findViewById<TextView>(R.id.hour_cell)
        timeTextView.text = CalendarUtils.formattedShortTime(time)
    }

    private fun setEvents(convertView : View, events : List<Event>) {
        val tv1 = convertView.findViewById<TextView>(R.id.event_1)
        val tv2 = convertView.findViewById<TextView>(R.id.event_2)
        val tv3 = convertView.findViewById<TextView>(R.id.event_3)

        if (events.isEmpty()) {
            // if we have no events then both should be invisible
            setEventTextInvisible(tv1)
            setEventTextInvisible(tv2)
            setEventTextInvisible(tv3)
            return
        }
        tv1.setOnClickListener{
            onCellEventClicked(events[0].id)
        }
        tv2.setOnClickListener{
            onCellEventClicked(events[1].id)
        }
        tv3.setOnClickListener{
            onCellEventClicked(events[2].id)
        }
        if (events.size == 1) {
            // if we have one then only first should be visible
            var class1 = SharedData.classList.value!!.find { item -> item.id == events[0].classesItemId }
            if (class1 == null) class1 = SharedData.defaultClass
            setEventTextVisible(tv1, events[0].name, class1.color)
            setEventTextInvisible(tv2)
            setEventTextInvisible(tv3)
        } else if (events.size == 2) {
            // if we have two, both should be visible
            var class1 = SharedData.classList.value!!.find { item -> item.id == events[0].classesItemId }
            var class2 = SharedData.classList.value!!.find { item -> item.id == events[1].classesItemId }
            if (class1 == null) class1 = SharedData.defaultClass
            if (class2 == null) class2 = SharedData.defaultClass
            setEventTextVisible(tv1, events[0].name, class1.color)
            setEventTextVisible(tv2, events[1].name, class2.color)
            setEventTextInvisible(tv3)
        } else if (events.size == 3) {
            var class1 = SharedData.classList.value!!.find { item -> item.id == events[0].classesItemId }
            var class2 = SharedData.classList.value!!.find { item -> item.id == events[1].classesItemId }
            var class3 = SharedData.classList.value!!.find { item -> item.id == events[2].classesItemId }
            if (class1 == null) class1 = SharedData.defaultClass
            if (class2 == null) class2 = SharedData.defaultClass
            if (class3 == null) class3 = SharedData.defaultClass
            setEventTextVisible(tv1, events[0].name, class1.color)
            setEventTextVisible(tv2, events[1].name, class2.color)
            setEventTextVisible(tv3, events[2].name, class3.color)
        } else {
            // if we have more, both things should be visible but second should display how many more we have
            // TODO
            /*var class1 = SharedData.classList.value!!.find { item -> item.id == events[0].classesItemId }
            if (class1 == null) class1 = SharedData.defaultClass
            setEventTextVisible(eventTextView1, events[0].name, class1.color)
            setEventTextVisible(eventTextView2, "+"+(events.size-1), context.getThemeColor(R.color.transparent))*/
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