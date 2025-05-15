package com.example.studentapp.ui.assignments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentapp.R
import com.example.studentapp.databinding.FragmentAssignmentsBinding
import com.example.studentapp.databinding.FragmentClassesBinding
import com.example.studentapp.ui.classesItem.ClassesItem
import kotlin.random.Random

class AssignmentsFragment : Fragment() {

    private var _binding: FragmentAssignmentsBinding? = null
    private lateinit var adapter: AssignmentsAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAssignmentsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // gets adapter
        adapter = AssignmentsAdapter {
            item ->
            val action = AssignmentsFragmentDirections.actionNavigationAssignmentsToFragmentEditAssignment(item.getClass().id.toString())
            // if we move back to classes using the bottomnav, we want to go to classes
            val navOptions = androidx.navigation.NavOptions.Builder()
                .setPopUpTo(R.id.navigation_assignments, true) // keeps StopwatchFragment in back stack
                .build()
            findNavController().navigate(action, navOptions)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        // visually divide classes with a vertical line
        val dividerItemDecoration = DividerItemDecoration(
            binding.recyclerView.context,
            DividerItemDecoration.VERTICAL
        )

        binding.recyclerView.addItemDecoration(dividerItemDecoration)

        // when the addButton is pressed, we create a new class
        binding.addButton.setOnClickListener {
            newAssignment()
        }
    }

    private fun newAssignment() {
        // navigate to event edit fragment
        val action = AssignmentsFragmentDirections.actionNavigationAssignmentsToFragmentEditAssignment("-1")
        // if we move back to classes using the bottomnav, we want to go to classes
        val navOptions = androidx.navigation.NavOptions.Builder()
            .setPopUpTo(R.id.navigation_assignments, true) // keeps StopwatchFragment in back stack
            .build()
        findNavController().navigate(action, navOptions)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}