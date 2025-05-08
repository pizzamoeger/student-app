package com.example.studentapp.ui.timetable

import android.R
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studentapp.databinding.CalendarDayCellBinding
import com.example.studentapp.ui.classesItem.ClassesItem

class CalendarAdapter (
    private val daysOfMonth: List<String>,
    private val onItemListener: (Int, String) -> Unit
): RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    inner class CalendarViewHolder (
        val binding: CalendarDayCellBinding
    ): RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        val dayOfMonth = binding.cellDayText

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
        return daysOfMonth.size
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.dayOfMonth.text = daysOfMonth[position]
    }


}