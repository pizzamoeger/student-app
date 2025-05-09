package com.example.studentapp.ui.timetable

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studentapp.R
import com.example.studentapp.SharedData
import com.example.studentapp.databinding.CalendarDayCellBinding
import com.example.studentapp.ui.classesItem.ClassesItem
import com.example.studentapp.ui.getThemeColor
import java.time.LocalDate

class CalendarAdapter (
    private val daysOfMonthText: List<LocalDate>,
    private val daysOfMonthSelected: List<Boolean>,
    private val context:  android.content.Context,
    private val onItemListener: (Int, String) -> Unit
): RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    inner class CalendarViewHolder (
        val binding: CalendarDayCellBinding
    ): RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        val dayOfMonth = binding.cellDayText
        val parentView = binding.cellDayParent

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            onItemListener(adapterPosition, dayOfMonth.text as String)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CalendarDayCellBinding.inflate(inflater, parent, false)
        val layoutParams = binding.root.layoutParams
        layoutParams.height = ((parent.height/6.0).toInt())
        return CalendarViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return daysOfMonthText.size
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val day = daysOfMonthText[position]
        holder.dayOfMonth.text = day.dayOfMonth.toString()

        if (day==SharedData.today.value) {
            holder.parentView.setBackgroundColor(context.getThemeColor(R.attr.windowBackgroundMuted))
        }

        // TODO can do this without daysOfMonthSelected
        if (daysOfMonthSelected[position]) holder.dayOfMonth.setTextColor(context.getThemeColor(android.R.attr.textColorPrimary))
        else holder.dayOfMonth.setTextColor(context.getThemeColor(R.attr.textColorPrimaryMuted))
    }


}