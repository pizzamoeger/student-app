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
import com.example.studentapp.databinding.FragmentClassesBinding
import com.example.studentapp.ui.classesItem.ClassesItem
import kotlin.random.Random

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

    // get random color
    private fun randomColor(): Int {
        val random = Random.Default
        val hsv = floatArrayOf(random.nextFloat()*360, 0.8F, 0.9F)
        return android.graphics.Color.HSVToColor(hsv)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // gets adapter
        adapter = ClassesAdapter (onDeleteClick = { id ->
            ClassesItem.delete(id)
            onViewCreated(view, savedInstanceState)},
            onClassesItemClick = {item ->
                val navController = findNavController()
                val action = ClassesFragmentDirections.actionClassesToEditClass(item.id.toString())

                /*val navOptions = androidx.navigation.NavOptions.Builder()
                    .setPopUpTo(R.id.navigation_classes, true) // keeps StopwatchFragment in back stack
                    .build()
                navController.navigate(action, navOptions)*/navController.navigate(action)})

        binding.recyclerViewClasses.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewClasses.adapter = adapter

        // visually divide classes with a vertical line
        val dividerItemDecoration = DividerItemDecoration(
            binding.recyclerViewClasses.context,
            DividerItemDecoration.VERTICAL
        )

        binding.recyclerViewClasses.addItemDecoration(dividerItemDecoration)

        // each time classList changes we call adapter.submitList
        /*SharedData.classesList.observe(viewLifecycleOwner) { classList ->
            adapter.submitList(classList)
        }*/

        // when the addButton is pressed, we create a new class
        binding.addButtonClasses.setOnClickListener {
            newClass()
        }
    }

    private fun newClass() {
        // navigate to event edit fragment
        val newClass = ClassesItem.add("Class ${System.currentTimeMillis() % 1000}", randomColor())
        val action = ClassesFragmentDirections.actionClassesToEditClass(newClass.id.toString())
        // if we move back to classes using the bottomnav, we want to go to classes
        // TODO findNavController().popBackStack()
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}