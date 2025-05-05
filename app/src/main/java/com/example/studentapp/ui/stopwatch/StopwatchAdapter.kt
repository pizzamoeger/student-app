package com.example.studentapp.ui.classes

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.studentapp.R
import com.example.studentapp.databinding.ItemClassBinding
import com.example.studentapp.ui.classesItem.ClassesItem

// bridge between ClassesItem and RecyclerView, which displays each class
class StopwatchAdapter (
    private val onStartClick: (ClassesItem) -> Unit,
    private val lifecycleOwner: LifecycleOwner,
    private val onClassesItemClick: (ClassesItem) -> Unit)
    : RecyclerView.Adapter<StopwatchAdapter.StopwatchViewHolder> () {

    private var classesList : List<ClassesItem> = emptyList()

    // is called each time the list in sharedViewModel changes
    fun submitList(list: List<ClassesItem>) {
        classesList = list
        notifyDataSetChanged()
    }

    // view holder for class item
    inner class StopwatchViewHolder(
        itemView : View,
        private val lifecycleOwner: LifecycleOwner,
        //TODO private val binding: ItemClassBinding
    ) : RecyclerView.ViewHolder(itemView) {
        // each class has a name and a delete button
        // TODO use binding for this: constructor where we set binding
        private val nameText: TextView = itemView.findViewById(R.id.name_text_classes_item)
        private val dailyTime: TextView = itemView.findViewById(R.id.daily_time_classes_item)
        private val startButton: Button = itemView.findViewById(R.id.timer_start_button_classes_item)

        fun bind(item: ClassesItem) {
            // bind name
            nameText.text = item.name

            // bind time
            item.text.observe(lifecycleOwner) {
                dailyTime.text = it
            }

            // bind button
            startButton.visibility = View.VISIBLE
            item.tracking.observe(lifecycleOwner) {
                // TODO use strings.xml for this
                if (it) startButton.text = "Stop"
                else startButton.text = "Start"
            }
            startButton.setOnClickListener {
                onStartClick(item)
            }

            // bind item click
            itemView.setOnClickListener {
                onClassesItemClick(item)
            }
        }
    }

    // creates view holder for a classItem
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StopwatchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_class, parent, false)
        //TODO val binding = ItemClassBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StopwatchViewHolder(view, lifecycleOwner)
    }

    // binds each item in list to a viewHolder
    override fun onBindViewHolder(
        holder: StopwatchViewHolder,
        position: Int) {
        holder.bind(classesList[position])
    }

    // how many items to display
    override fun getItemCount(): Int = classesList.size
}