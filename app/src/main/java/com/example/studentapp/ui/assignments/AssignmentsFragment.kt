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
import com.example.studentapp.ui.getThemeColor
import com.example.studentapp.ui.stopwatch.StopwatchFragmentDirections
import kotlin.random.Random

class AssignmentsFragment : Fragment(), AssignmentsAdapter.AssignmentAdapterListener {

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
        adapter = AssignmentsAdapter ({
                item ->
            val action = AssignmentsFragmentDirections.actionNavigationAssignmentsToFragmentEditAssignment(item.getId().toString())
            // if we move back to classes using the bottomnav, we want to go to classes
            val navOptions = androidx.navigation.NavOptions.Builder()
                .setPopUpTo(R.id.navigation_assignments, true) // keeps StopwatchFragment in back stack
                .build()
            findNavController().navigate(action, navOptions)
        }, listener = this,
            { item ->
                val action = AssignmentsFragmentDirections.actionNavigationAssignmentsToNavigationAssignment(item.getId().toString())
                findNavController().navigate(action)})

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

    override fun onResume() {
        super.onResume()
        // display top navigation
        val toolbar = binding.includedToolbar

        // left selection links to nothing (is current)
        context?.let { toolbar.textLeft.setTextColor(it.getThemeColor(androidx.appcompat.R.attr.colorPrimary)) }
        context?.let { toolbar.lineLeft.setBackgroundColor(it.getThemeColor(androidx.appcompat.R.attr.colorPrimary)) }
        toolbar.selectionLeft.setOnClickListener {}

        // right selection links to insight
        context?.let { toolbar.textRight.setTextColor(it.getThemeColor(com.google.android.material.R.attr.colorOnSurface)) }
        context?.let { toolbar.lineRight.setBackgroundColor(it.getThemeColor(com.google.android.material.R.attr.colorOnSurface)) }
        toolbar.selectionRight.setOnClickListener {
            val navController = findNavController()
            val action = AssignmentsFragmentDirections.actionNavigationAssignmentsToFragmentMonthView()
            navController.navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onRequestAdapterRefresh() {
        adapter.notifyDataSetChanged()
    }
}