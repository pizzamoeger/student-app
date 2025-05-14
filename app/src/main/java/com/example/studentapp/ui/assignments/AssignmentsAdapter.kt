package com.example.studentapp.ui.assignments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studentapp.R
import com.example.studentapp.ui.assignments.assignment.Assignment
import com.example.studentapp.ui.classesItem.ClassesItem

class AssignmentsAdapter : RecyclerView.Adapter<AssignmentsAdapter.AssignmentsViewHolder> ()  {
    inner class AssignmentsViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        private val text: TextView = itemView.findViewById(R.id.assignment_text)
        private val colorBlock: View = itemView.findViewById(R.id.assignment_class_color)

        fun bind(item: Assignment) {
            // bind name
            text.text = item.getTitle()

            // bind delete button
            colorBlock.setBackgroundColor(item.getClass().color)

            // bind item click
            itemView.setOnClickListener {
                // TODO
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssignmentsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.assignment, parent, false)
        return AssignmentsViewHolder(view)
    }

    override fun getItemCount() = Assignment.getList().size

    override fun onBindViewHolder(holder: AssignmentsViewHolder, position: Int) {
        holder.bind(Assignment.getByIndex(position))

    }
}