package com.hannah.studentapp.ui.grades

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hannah.studentapp.databinding.GradesBinding
import com.hannah.studentapp.ui.classesItem.ClassesItem

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
        adapter = GradesForClassAdapter(requireContext())
        binding.recyclerViewGrades.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewGrades.adapter = adapter
        val root = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addButtonGrade.setOnClickListener{
            val navController = findNavController()
            val action = GradesForClassFragmentDirections.actionGradesForClassFragmentToAddGradeFragment(ClassesItem.getCurrent().getId())
            navController.navigate(action)
        }
    }
}