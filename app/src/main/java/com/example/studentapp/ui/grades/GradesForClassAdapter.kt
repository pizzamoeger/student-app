package com.example.studentapp.ui.grades

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.studentapp.databinding.ClassItemGradesBinding
import com.example.studentapp.databinding.GradeItemBinding
import com.example.studentapp.databinding.GradesBinding
import com.example.studentapp.ui.classesItem.ClassesItem

class GradesForClassAdapter
    : RecyclerView.Adapter<GradesForClassAdapter.ClassesViewHolder> () {

    inner class ClassesViewHolder(binding : GradeItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val name = binding.name
        val weight = binding.weightInput
        val grade = binding.gradeInput
        val del = binding.deleteButtonGrade
        fun bind(item: Pair<Float, Float>) {

            // bind name
            name.setText("tmp") // TODO

            del.setOnClickListener{
                // TODO
            }

            // bind average
            weight.setText(String.format("%.2f", item.second))
            grade.setText(String.format("%.2f", item.first))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassesViewHolder {
        val binding = GradeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClassesViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return ClassesItem.getList().size
    }

    override fun onBindViewHolder(holder: ClassesViewHolder, position: Int) {
        holder.bind(Pair(0.1f, 0.1f)) // TODO
    }
}