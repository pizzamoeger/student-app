package com.hannah.studentapp.ui.classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hannah.studentapp.databinding.ItemClassClassesBinding
import com.hannah.studentapp.ui.classesItem.ClassesItem

// bridge between ClassesItem and RecyclerView, which displays each class
class ClassesAdapter (
    private val onClassesItemClick: (ClassesItem) -> Unit)
    : RecyclerView.Adapter<ClassesAdapter.ClassesViewHolder> () {

    // view holder for class item
    inner class ClassesViewHolder(binding : ItemClassClassesBinding) : RecyclerView.ViewHolder(binding.root) {
        // each class has a name and a delete button
        private val nameText: TextView = binding.nameTextClassesItem
        private val editButton: ImageButton = binding.editButtonClasses
        private val classColor: View = binding.classesClassColor

        fun bind(item: ClassesItem) {
            classColor.setBackgroundColor(item.getColor())

            // bind name
            nameText.text = item.toString()

            // bind item click
            editButton.setOnClickListener {
                onClassesItemClick(item)
            }
        }
    }

    // creates view holder for a classItem
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassesViewHolder {
        val binding = ItemClassClassesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClassesViewHolder(binding)
    }

    // binds each item in list to a viewHolder
    override fun onBindViewHolder(
        holder: ClassesViewHolder,
        position: Int) {
        holder.bind(ClassesItem.getByIndex(position))
    }

    // how many items to display
    override fun getItemCount(): Int = ClassesItem.getList().size
}