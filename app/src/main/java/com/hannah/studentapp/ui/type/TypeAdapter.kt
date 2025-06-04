package com.hannah.studentapp.ui.type

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannah.studentapp.databinding.AssignmentBinding
import com.hannah.studentapp.databinding.EctsTypeItemBinding

class TypeAdapter (): RecyclerView.Adapter<TypeAdapter.TypeViewHolder> () {
    inner class TypeViewHolder(
        private val binding: EctsTypeItemBinding
    ):RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Type) {
            binding.ectsTypeItemName.text = item.getName()
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