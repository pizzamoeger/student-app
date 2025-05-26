package com.example.studentapp.ui.assignments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studentapp.R
import com.example.studentapp.databinding.AssignmentBinding
import com.example.studentapp.ui.assignments.assignment.Assignment
import com.example.studentapp.ui.getThemeColor
import java.time.LocalDate

class AssignmentsAdapter (
    private val onEditClick: (Assignment) -> Unit,
    private val onItemClick: (Assignment) -> Unit,
    private val assignments: List<Assignment>
    ): RecyclerView.Adapter<AssignmentsAdapter.AssignmentsViewHolder> ()  {

    inner class AssignmentsViewHolder(
        private val binding: AssignmentBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private val text: TextView = binding.assignmentText
        private val colorBlock: View = binding.assignmentClassColor
        private val dueDate: TextView = binding.dueDate
        private val edit: ImageButton = binding.editButtonAssignment
        private val progressBar : ProgressBar = binding.progressBar

        fun bind(item: Assignment) {
            // bind name
            text.text = item.getTitle()
            if (item.getDueDate() < LocalDate.now()) {
                text.setTextColor(binding.root.context.getColor(R.color.red))
                dueDate.setTextColor(binding.root.context.getColor(R.color.red))
            } else {
                text.setTextColor(binding.root.context.getThemeColor(android.R.attr.textColor))
                dueDate.setTextColor(binding.root.context.getThemeColor(android.R.attr.textColor))
            }

            dueDate.text = item.getDueDate().toString()

            // bind delete button
            colorBlock.setBackgroundColor(item.getClass().getColor())

            //rv.setProgressBar(R.id.progress_bar_widget, 100, item.getProgress(), false)
            progressBar.progress = item.getProgress()
            progressBar.max = 100

            // bind item click
            edit.setOnClickListener {
                onEditClick(item)
            }
            itemView.setOnClickListener{
                //item.setProgress(item.getProgress()+0.05) // TODO temp
                //bind(item)
                onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssignmentsViewHolder {
        val binding = AssignmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AssignmentsViewHolder(binding)
    }

    override fun getItemCount() : Int {
        return assignments.size
    }

    override fun onBindViewHolder(holder: AssignmentsViewHolder, position: Int) {
        holder.bind(assignments[position])

    }
}