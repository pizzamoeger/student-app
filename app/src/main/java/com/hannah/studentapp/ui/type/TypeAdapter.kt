package com.hannah.studentapp.ui.type

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hannah.studentapp.R
import com.hannah.studentapp.databinding.AssignmentBinding
import com.hannah.studentapp.databinding.EctsTypeItemBinding
import com.hannah.studentapp.ui.getThemeColor

class TypeAdapter (): RecyclerView.Adapter<TypeAdapter.TypeViewHolder> () {
    inner class TypeViewHolder(
        private val binding: EctsTypeItemBinding
    ):RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Type) {
            binding.ectsTypeItemName.text = item.getName()
            binding.ectsTypeItemPending.text = item.getOngoingECTS().toString()
            binding.ectsTypeItemNeeded.text = (item.getECTSNeeded()-item.getCompletedECTS()).toString()
            if (item.getECTSNeeded()-item.getCompletedECTS() < 0) {
                val context = binding.root.context
                val baseColor = ContextCompat.getColor(context, R.color.green)
                val transparentColor = Color.argb(100, Color.red(baseColor), Color.green(baseColor), Color.blue(baseColor))
                binding.root.setBackgroundColor(transparentColor)
            }
            //binding.ectsTypeItemPassed.text = item.getCompletedECTS().toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeViewHolder {
        val binding = EctsTypeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TypeViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return Type.getList().size
    }

    override fun onBindViewHolder(holder: TypeViewHolder, position: Int) {
        holder.bind(Type.getList()[position])
    }

}