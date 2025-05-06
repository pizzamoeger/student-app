package com.example.studentapp.ui.classes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentapp.SharedData
import com.example.studentapp.databinding.FragmentClassesBinding
import com.example.studentapp.ui.stopwatch.StopwatchFragmentDirections

class ClassesFragment : Fragment() {

    private var _binding: FragmentClassesBinding? = null
    private lateinit var adapter: ClassesAdapter
    private val classesViewModel : ClassesViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClassesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // gets adapter
        adapter = ClassesAdapter (onDeleteClick = {id -> SharedData.deleteClass(id)},
            onClassesItemClick = {item ->
                val action = ClassesFragmentDirections.actionClassesToClassesItem(item.id.toString())
                findNavController().navigate(action)})

        binding.recyclerViewClasses.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewClasses.adapter = adapter

        // each time classList changes we call adapter.submitList
        SharedData.classList.observe(viewLifecycleOwner) { classList ->
            adapter.submitList(classList)
        }

        // when the addButton is pressed, we create a new class
        binding.addButtonClasses.setOnClickListener {
            SharedData.addClass("Class ${System.currentTimeMillis() % 1000}")
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}