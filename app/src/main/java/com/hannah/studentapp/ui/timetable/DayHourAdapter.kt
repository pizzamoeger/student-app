package com.hannah.studentapp.ui.timetable

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.hannah.studentapp.R
import com.hannah.studentapp.ui.calendar.CalendarUtils
import com.hannah.studentapp.ui.classesItem.ClassesItem
import com.hannah.studentapp.ui.event.Event
import java.time.LocalTime

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
        val linearLayout = convertView.findViewById<LinearLayout>(R.id.linear_layout)
        linearLayout.removeAllViews()
        for (event in events) {
            val classItem = ClassesItem.get(event.getClassId())

            val textView = TextView(context).apply {
                text = event.getName()
                gravity = Gravity.CENTER
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                setLines(2)
                setTextColor(
                    TypedValue()
                        .apply { context.theme.resolveAttribute(android.R.attr.textColorPrimary, this, true) }
                        .data
                )
                setBackgroundColor(classItem.getColor())

                layoutParams = LinearLayout.LayoutParams(
                    0, // width
                    LinearLayout.LayoutParams.MATCH_PARENT
                ).apply {
                    weight = 1f
                    val marginPx = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 0.75f, context.resources.displayMetrics
                    ).toInt()
                    setMargins(marginPx, marginPx, marginPx, marginPx)
                }
            }

            textView.setOnClickListener{
                onCellEventClicked(event.getId())
            }

            linearLayout.addView(textView)
        }
    }
}