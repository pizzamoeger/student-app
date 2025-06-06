package com.hannah.studentapp.ui.grades

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hannah.studentapp.databinding.FragmentGradesBinding
import com.hannah.studentapp.ui.classesItem.ClassesItem

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
        adapter = GradesClassesAdapter({ item ->
            val navController = findNavController()
            val action = GradesFragmentDirections.actionFragmentGradesToAddGradeFragment(item.getId())
            navController.navigate(action)
        }, {
                item ->
            val navController = findNavController()
            val action = GradesFragmentDirections.actionFragmentGradesToGradesForClassFragment(item.getId())
            ClassesItem.switch(item, false)
            navController.navigate(action)
        })
        binding.recyclerViewClasses.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewClasses.adapter = adapter
        val root: View = binding.root
        return root
    }
}