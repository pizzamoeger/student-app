package com.example.studentapp.ui.grades

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentapp.databinding.FragmentGradesBinding
import com.example.studentapp.databinding.GradesBinding
import com.example.studentapp.ui.classesItem.ClassesItem

class GradesForClassFragment : Fragment() {
    private var _binding: GradesBinding? = null
    private lateinit var adapter: GradesForClassAdapter
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = GradesBinding.inflate(inflater, container, false)
        adapter = GradesForClassAdapter()
        binding.recyclerViewGrades.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewGrades.adapter = adapter
        val root = binding.root
        return root
    }
}