package com.example.studentapp.ui.classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studentapp.R
import com.example.studentapp.SharedData
import com.example.studentapp.ui.classesItem.ClassesItem

// bridge between ClassesItem and RecyclerView, which displays each class
class ClassesAdapter (
    private val onDeleteClick: (Int) -> Unit,
    private val onClassesItemClick: (ClassesItem) -> Unit)
    : RecyclerView.Adapter<ClassesAdapter.ClassesViewHolder> () {

    private var classesList : List<ClassesItem> = emptyList()

    // is called each time the list in sharedViewModel changes
    fun submitList(list: List<ClassesItem>) {
        classesList = list
        notifyDataSetChanged()
    }

    // view holder for class item
    inner class ClassesViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        // each class has a name and a delete button
        // TODO use binding for this: constructor where we set binding
        private val nameText: TextView = itemView.findViewById(R.id.name_text_classes_item)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.delete_button_classes_item)

        fun bind(item: ClassesItem) {
            // bind name
            nameText.text = item.name

            // bind delete button
            deleteButton.visibility = View.VISIBLE
            deleteButton.setOnClickListener {
                onDeleteClick(item.id)
            }

            // bind item click
            itemView.setOnClickListener {
                onClassesItemClick(item)
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
        holder.bind(ClassesItem.classesList[position])
    }

    // how many items to display
    override fun getItemCount(): Int = ClassesItem.classesList.size
}