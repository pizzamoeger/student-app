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
import com.example.studentapp.ui.classesItem.ClassesItem
import com.example.studentapp.ui.event.Event
import com.example.studentapp.ui.getThemeColor
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters

class WeekHourAdapter (
    context : Context,
    hourEvents : List<HourEvent>,
    private val onCellClicked: (LocalDate, LocalTime) -> Unit,
    private val onCellEventClicked: (LocalDate) -> Unit)
: ArrayAdapter<HourEvent?>(context, 0, hourEvents) {

    override fun getView(position: Int, convertViewArg: View?, parent: ViewGroup): View {
        // set view
        val event = getItem(position)

        var convertView = convertViewArg
        if (convertView == null) {
            // if view is null we have to create it
            convertView = LayoutInflater.from(context).inflate(R.layout.hour_cell_week, parent, false)
        }

        // set the correct hour
        setHour(convertView!!, event!!.time)
        // set all events for this week
        setEventsWeek(convertView, event.events)
        // bind all cells
        bindCells(convertView, event.time)

        return convertView
    }

    private fun bindCells(convertView : View, time: LocalTime) {
        val monCell = convertView.findViewById<LinearLayout>(R.id.linear_layout_mon)
        val tueCell = convertView.findViewById<LinearLayout>(R.id.linear_layout_tue)
        val wedCell = convertView.findViewById<LinearLayout>(R.id.linear_layout_wed)
        val thurCell = convertView.findViewById<LinearLayout>(R.id.linear_layout_thur)
        val friCell = convertView.findViewById<LinearLayout>(R.id.linear_layout_fri)

        // make all clickable
        val day = CalendarUtils.selectedDate.with(
            TemporalAdjusters.previousOrSame(
                DayOfWeek.MONDAY))

        monCell.setOnClickListener{
            onCellClicked(day, time)
        }
        tueCell.setOnClickListener{
            onCellClicked(day.plusDays(1), time)
        }
        wedCell.setOnClickListener{
            onCellClicked(day.plusDays(2), time)
        }
        thurCell.setOnClickListener{
            onCellClicked(day.plusDays(3), time)
        }
        friCell.setOnClickListener{
            onCellClicked(day.plusDays(4), time)
        }
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
            return
        }
        eventTextView1.setOnClickListener{
            onCellEventClicked(events[0].date)
        }
        eventTextView2.setOnClickListener{
            onCellEventClicked(events[0].date)
        }
        if (events.size == 1) {
            // if we have one then only first should be visible
            val class1 = ClassesItem.get(events[0].classesItemId)
            setEventTextVisible(eventTextView1, events[0].name, class1.getColor())
            setEventTextInvisible(eventTextView2)
        } else if (events.size == 2) {
            // if we have two, both should be visible
            val class1 = ClassesItem.get(events[0].classesItemId)
            val class2 = ClassesItem.get(events[1].classesItemId)
            setEventTextVisible(eventTextView1, events[0].name, class1.getColor())
            setEventTextVisible(eventTextView2, events[1].name, class2.getColor())
        } else {
            // if we have more, both things should be visible but second should display how many more we have
            val class1 = ClassesItem.get(events[0].classesItemId)
            setEventTextVisible(eventTextView1, events[0].name, class1.getColor())
            setEventTextVisible(eventTextView2, "+"+(events.size-1), context.getThemeColor(R.color.transparent))
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