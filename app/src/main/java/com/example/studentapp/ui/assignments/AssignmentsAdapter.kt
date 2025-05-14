package com.example.studentapp.ui.assignments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.studentapp.R

class AssignmentsAdapter : RecyclerView.Adapter<AssignmentsAdapter.AssignmentsViewHolder> ()  {
    inner class AssignmentsViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssignmentsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_class, parent, false)
        return AssignmentsViewHolder(view)
    }

    override fun getItemCount(): Int {
        //TODO("Not yet implemented")
        return 1
    }

    override fun onBindViewHolder(holder: AssignmentsViewHolder, position: Int) {
        //TODO("Not yet implemented")
    }
}