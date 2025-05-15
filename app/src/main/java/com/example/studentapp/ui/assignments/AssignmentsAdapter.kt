package com.example.studentapp.ui.assignments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studentapp.R
import com.example.studentapp.databinding.AssignmentBinding
import com.example.studentapp.databinding.ItemClassBinding
import com.example.studentapp.ui.assignments.assignment.Assignment
import com.example.studentapp.ui.classesItem.ClassesItem
import com.example.studentapp.ui.getThemeColor
import java.time.LocalDate

class AssignmentsAdapter (
    private val onItemClick: (Assignment) -> Unit,
    private val listener: AssignmentAdapterListener
): RecyclerView.Adapter<AssignmentsAdapter.AssignmentsViewHolder> ()  {

    interface AssignmentAdapterListener {
        fun onRequestAdapterRefresh()
    }

    inner class AssignmentsViewHolder(
        private val binding: AssignmentBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private val text: TextView = binding.assignmentText
        private val colorBlock: View = binding.assignmentClassColor
        private val dueDate: TextView = binding.dueDate
        private val edit: ImageButton = binding.editButtonAssignment
        private val progressComp : View = binding.progressCompleted
        private val progressUncomp : View = binding.progressUncompleted

        fun bind(item: Assignment) {
            if (item.isCompleted()) {
                listener.onRequestAdapterRefresh()
                return
            }
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
            colorBlock.setBackgroundColor(item.getClass().color)

            val paramsC = progressComp.layoutParams as LinearLayout.LayoutParams
            paramsC.weight=item.getProgress().toFloat()
            progressComp.layoutParams = paramsC

            val paramsU = progressUncomp.layoutParams as LinearLayout.LayoutParams
            paramsU.weight=1-item.getProgress().toFloat()
            progressUncomp.layoutParams = paramsU

            // bind item click
            edit.setOnClickListener {
                onItemClick(item)
            }
            itemView.setOnClickListener{
                item.setProgress(item.getProgress()+0.05) // TODO temp
                Assignment.save()
                bind(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssignmentsViewHolder {
        val binding = AssignmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AssignmentsViewHolder(binding)
    }

    override fun getItemCount() : Int {
        val list = Assignment.getList()
        var count = 0
        for (assignment in list) {
            if (assignment.isCompleted()) continue
            count++
        }
        return count
    }

    override fun onBindViewHolder(holder: AssignmentsViewHolder, position: Int) {
        holder.bind(Assignment.getUncompletedByIndex(position)!!)

    }
}