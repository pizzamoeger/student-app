package com.example.studentapp.ui.classes

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.studentapp.R
import com.example.studentapp.SharedData
import com.example.studentapp.databinding.ItemClassStopwatchBinding
import com.example.studentapp.ui.classesItem.ClassesItem

// bridge between ClassesItem and RecyclerView, which displays each class
class StopwatchAdapter (
    private val onStartClick: (ClassesItem) -> Unit,
    private val lifecycleOwner: LifecycleOwner,
    private val onClassesItemClick: (ClassesItem) -> Unit)
    : RecyclerView.Adapter<StopwatchAdapter.StopwatchViewHolder> () {

    // view holder for class item
    inner class StopwatchViewHolder(
        private val binding: ItemClassStopwatchBinding,
        private val lifecycleOwner: LifecycleOwner,
    ) : RecyclerView.ViewHolder(binding.root) {

        // each class has a name and a delete button
        private val nameText: TextView = binding.nameTextClassesItem
        private val dailyTime: TextView = binding.dailyTimeClassesItem
        private val startButton: ImageButton = binding.timerStartButtonClassesItem

        fun bind(item: ClassesItem) {
            // bind name
            nameText.text = item.toString()

            // bind time
            item.text.observe(lifecycleOwner) {
                dailyTime.text = it
            }

            // get background for buttons
            val drawable = ContextCompat.getDrawable(binding.root.context, R.drawable.circle_background)
            val colorFilter = PorterDuffColorFilter(item.getColor(), PorterDuff.Mode.SRC_IN)
            if (drawable != null) {
                drawable.colorFilter = colorFilter
            }
            startButton.background = drawable

            // set the icons for buttons
            item.tracking.observe(lifecycleOwner) {
                if (it) { // tracking is true
                    startButton.setImageResource(R.drawable.pause)
                } else { // tracking is false
                    startButton.setImageResource(R.drawable.start)
                }
            }

            // bind buttons
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
        val binding = ItemClassStopwatchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StopwatchViewHolder(binding, lifecycleOwner)
    }

    // binds each item in list to a viewHolder
    override fun onBindViewHolder(
        holder: StopwatchViewHolder,
        position: Int) {
        holder.bind(ClassesItem.getByIndex(position))
    }

    // how many items to display
    override fun getItemCount(): Int = ClassesItem.getList().size
}