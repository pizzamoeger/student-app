package com.example.studentapp.ui.classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studentapp.R

// bridge between ClassesItem and RecyclerView, which displays each class
class ClassesAdapter (
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<ClassesAdapter.ClassesViewHolder> () {

    private var classesList : List<ClassesItem> = emptyList()

    fun submitList(list: List<ClassesItem>) {
        // is called each time the list in sharedViewModel changes
        classesList = list
        notifyDataSetChanged()
    }

    inner class ClassesViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        // each class has a name and a delete button
        // TODO use binding for this: constructor where we set binding
        private val nameText: TextView = itemView.findViewById(R.id.name_text_classes_item)
        private val deleteButton: Button = itemView.findViewById(R.id.delete_button_classes_item)

        fun bind(item: ClassesItem) {
            nameText.text = item.name
            deleteButton.setOnClickListener {
                onDeleteClick(item.id)
            }
        }
    }

    // creates view holder for a classItem
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_class, parent, false)
        return ClassesViewHolder(view)
    }

    // binds each item in list to a viewHolder
    override fun onBindViewHolder(
        holder: ClassesViewHolder,
        position: Int) {
        holder.bind(classesList[position])
    }

    // how many items to display
    override fun getItemCount(): Int = classesList.size
}