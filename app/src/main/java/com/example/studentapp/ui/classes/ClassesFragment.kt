package com.example.studentapp.ui.classes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentapp.R
import com.example.studentapp.databinding.FragmentClassesBinding
import com.example.studentapp.ui.classes.Class

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
        val viewModel = ViewModelProvider(this).get(ClassesViewModel::class.java)

        _binding = FragmentClassesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textClasses
        viewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = ClassesAdapter {id -> classesViewModel.deleteClass(id)}

        binding.classRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.classRecyclerView.adapter = adapter

        classesViewModel.classList.observe(viewLifecycleOwner) { classList ->
            adapter.submitList(classList)
        }

        binding.addButton.setOnClickListener {
            classesViewModel.addClass("Class ${System.currentTimeMillis() % 1000}")
        }
        Log.d("TAG", "ClassesFragment loaded")

    }*/


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}