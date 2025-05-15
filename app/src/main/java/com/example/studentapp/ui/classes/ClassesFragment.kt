package com.example.studentapp.ui.classes

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
import com.example.studentapp.databinding.FragmentClassesBinding
import com.example.studentapp.ui.classesItem.ClassesItem
import kotlin.random.Random

class ClassesFragment : Fragment() {

    private var _binding: FragmentClassesBinding? = null
    private lateinit var adapter: ClassesAdapter

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
        adapter = ClassesAdapter (
            onClassesItemClick = {item ->
                val navController = findNavController()
                val action = ClassesFragmentDirections.actionClassesToEditClass(item.getId().toString())

                val navOptions = androidx.navigation.NavOptions.Builder()
                    .setPopUpTo(R.id.navigation_classes, true) // keeps StopwatchFragment in back stack
                    .build()
                navController.navigate(action, navOptions)})

        binding.recyclerViewClasses.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewClasses.adapter = adapter

        // visually divide classes with a vertical line
        val dividerItemDecoration = DividerItemDecoration(
            binding.recyclerViewClasses.context,
            DividerItemDecoration.VERTICAL
        )

        binding.recyclerViewClasses.addItemDecoration(dividerItemDecoration)

        // when the addButton is pressed, we create a new class
        binding.addButtonClasses.setOnClickListener {
            newClass()
        }
    }

    private fun newClass() {
        // navigate to event edit fragment
        val action = ClassesFragmentDirections.actionClassesToEditClass("-1")
        // if we move back to classes using the bottomnav, we want to go to classes
        val navOptions = androidx.navigation.NavOptions.Builder()
            .setPopUpTo(R.id.navigation_classes, true) // keeps StopwatchFragment in back stack
            .build()
        findNavController().navigate(action, navOptions)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}