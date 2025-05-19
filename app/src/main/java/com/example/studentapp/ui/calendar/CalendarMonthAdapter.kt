package com.example.studentapp.ui.calendar

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.studentapp.R
import com.example.studentapp.SharedData
import com.example.studentapp.databinding.CalendarDayCellBinding
import com.example.studentapp.ui.assignments.assignment.Assignment
import com.example.studentapp.ui.calendar.CalendarUtils.Companion.selectedDate
import com.example.studentapp.ui.classesItem.ClassesItem
import com.example.studentapp.ui.getThemeColor
import java.time.LocalDate

class CalendarMonthAdapter (
    private val daysList: List<LocalDate>,
    private val context:  android.content.Context,
    private val onItemClick: (LocalDate) -> Unit
): RecyclerView.Adapter<CalendarMonthAdapter.CalendarViewHolder>() {

    inner class CalendarViewHolder (
        val binding: CalendarDayCellBinding
    ): RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        val dayOfMonth = binding.cellDayText
        val parentView = binding.cellDayParent

        init {
            // makes this clickable
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            onItemClick(daysList[adapterPosition])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CalendarDayCellBinding.inflate(inflater, parent, false)

        // there are 6 rows, each should fill the same space
        val layoutParams = binding.root.layoutParams
        layoutParams.height = ((parent.height/6.0).toInt())

        return CalendarViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return daysList.size
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val day = daysList[position]

        // set text (number of day in month)
        val dayStr = day.dayOfMonth.toString()
        holder.dayOfMonth.text = dayStr

        // if it is selected we want to highlight it
        if (day == selectedDate) {
            holder.parentView.setBackgroundColor(context.getThemeColor(R.attr.windowBackgroundMuted))
        }

        // text color different when day not in month of selected day
        if (daysList[position].month == selectedDate.month) {
            holder.dayOfMonth.setTextColor(context.getThemeColor(android.R.attr.textColorPrimary))
        }
        else {
            holder.dayOfMonth.setTextColor(context.getThemeColor(R.attr.textColorPrimaryMuted))
        }

        // assignments
        val flexLayout = holder.binding.flexboxLayout
        for (assignment in Assignment.getUncompletedDay(day)) {
            val classItem = assignment.getClass()

            val drawable = ContextCompat.getDrawable(holder.binding.root.context, R.drawable.circle_background)
            val colorFilter = PorterDuffColorFilter(classItem.getColor(), PorterDuff.Mode.SRC_IN)
            if (drawable != null) {
                drawable.colorFilter = colorFilter
            }

            val view = View(context).apply {
                layoutParams = ViewGroup.MarginLayoutParams(10, 10).apply {
                    setMargins(7, 7, 7, 7)
                }
                background = drawable
            }

            flexLayout.addView(view)
        }
    }
}