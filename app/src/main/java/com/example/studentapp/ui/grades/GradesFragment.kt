package com.example.studentapp.ui.grades

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentapp.databinding.FragmentGradesBinding

class GradesFragment : Fragment() {
    private var _binding: FragmentGradesBinding? = null
    private lateinit var adapter: GradesClassesAdapter
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGradesBinding.inflate(inflater, container, false)
        adapter = GradesClassesAdapter()
        binding.recyclerViewClasses.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewClasses.adapter = adapter
        val root: View = binding.root
        return root
    }
}