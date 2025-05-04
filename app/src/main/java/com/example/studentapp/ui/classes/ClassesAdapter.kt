package com.example.studentapp.ui.classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studentapp.R

class ClassesAdapter (
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<ClassesAdapter.ClassesViewHolder> () {

    private var classesList : List<ClassesItem> = emptyList()

    fun submitList(list: List<ClassesItem>) {
        classesList = list
        notifyDataSetChanged()
    }

    inner class ClassesViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        // TODO use binding for this
        private val nameText: TextView = itemView.findViewById(R.id.name_text_classes_item)
        private val deleteButton: Button = itemView.findViewById(R.id.delete_button_classes_item)

        fun bind(item: ClassesItem) {
            nameText.text = item.name
            deleteButton.setOnClickListener {
                onDeleteClick(item.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_class, parent, false)
        return ClassesViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ClassesViewHolder,
        position: Int) {
        holder.bind(classesList[position])
    }

    override fun getItemCount(): Int = classesList.size
}