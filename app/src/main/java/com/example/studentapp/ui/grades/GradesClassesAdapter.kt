package com.example.studentapp.ui.grades

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.studentapp.databinding.ClassItemGradesBinding
import com.example.studentapp.ui.classesItem.ClassesItem

class GradesClassesAdapter (
    private val onAddClick: (ClassesItem) -> Unit,
    private val onClassesItemClick: (ClassesItem) -> Unit)
: RecyclerView.Adapter<GradesClassesAdapter.ClassesViewHolder> () {

    inner class ClassesViewHolder(binding : ClassItemGradesBinding) : RecyclerView.ViewHolder(binding.root) {
        val classColor = binding.classesClassColor
        val nameText = binding.nameTextClassesItem
        val average = binding.averageTextClassesItem
        val add = binding.addGrade
        fun bind(item: ClassesItem) {
            classColor.setBackgroundColor(item.getColor())

            // bind name
            nameText.text = item.toString()

            // bind average
            average.text = "ø --"
            val av = String.format("%.2f", item.getAverage())
            if (item.getAverage() >= 0) average.text = "ø ${av}"

            // bind item click
            add.setOnClickListener {
                onAddClick(item)
            }

            itemView.setOnClickListener {
                onClassesItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassesViewHolder {
        val binding = ClassItemGradesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClassesViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return ClassesItem.getList().size
    }

    override fun onBindViewHolder(holder: ClassesViewHolder, position: Int) {
        holder.bind(ClassesItem.getByIndex(position))
    }
}