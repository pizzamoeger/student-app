package com.example.studentapp.ui.assignments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studentapp.R
import com.example.studentapp.databinding.AssignmentBinding
import com.example.studentapp.databinding.ItemClassBinding
import com.example.studentapp.ui.assignments.assignment.Assignment
import com.example.studentapp.ui.classesItem.ClassesItem
import com.example.studentapp.ui.getThemeColor
import java.time.LocalDate

class AssignmentsAdapter : RecyclerView.Adapter<AssignmentsAdapter.AssignmentsViewHolder> ()  {
    inner class AssignmentsViewHolder(
        private val binding: AssignmentBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        private val text: TextView = binding.assignmentText
        private val colorBlock: View = binding.assignmentClassColor
        private val dueDate: TextView = binding.dueDate

        fun bind(item: Assignment) {
            // bind name
            text.text = item.getTitle()
            if (item.getDueDate() < LocalDate.now()) {
                text.setTextColor(binding.root.context.getColor(R.color.red))
                dueDate.setTextColor(binding.root.context.getColor(R.color.red))
            }

            dueDate.text = item.getDueDate().toString()

            // bind delete button
            colorBlock.setBackgroundColor(item.getClass().color)

            // bind item click
            itemView.setOnClickListener {
                // TODO
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssignmentsViewHolder {
        val binding = AssignmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return AssignmentsViewHolder(binding)
    }

    override fun getItemCount() = Assignment.getList().size

    override fun onBindViewHolder(holder: AssignmentsViewHolder, position: Int) {
        holder.bind(Assignment.getByIndex(position))

    }
}