package com.example.studentapp.ui.grades

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.example.studentapp.databinding.ClassItemGradesBinding
import com.example.studentapp.databinding.GradeItemBinding
import com.example.studentapp.databinding.GradesBinding
import com.example.studentapp.ui.classesItem.ClassesItem

class GradesForClassAdapter (private val context : Context)
    : RecyclerView.Adapter<GradesForClassAdapter.ClassesViewHolder> () {

    inner class ClassesViewHolder(binding : GradeItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val name = binding.name
        val weight = binding.weightInput
        val grade = binding.gradeInput
        val del = binding.deleteButtonGrade
        fun bind(item: Grade) {

            // bind name
            name.setText(item.name)
            name.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    item.name = s.toString()
                    ClassesItem.save(context)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            del.setOnClickListener{
                ClassesItem.getCurrent().deleteGrade(item.id, context)
                notifyDataSetChanged()
            }

            // bind average
            weight.setText(String.format("%.2f", item.weight))
            weight.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    item.weight = s.toString().toFloat()
                    ClassesItem.save(context)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
            grade.setText(String.format("%.2f", item.grade))
            grade.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    item.grade = s.toString().toFloat()
                    ClassesItem.save(context)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassesViewHolder {
        val binding = GradeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClassesViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return ClassesItem.getCurrent().getGrades().size
    }

    override fun onBindViewHolder(holder: ClassesViewHolder, position: Int) {
        holder.bind(ClassesItem.getCurrent().getGrades()[position])
    }
}