package com.example.studentapp.ui.stopwatch

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentapp.SharedData
import com.example.studentapp.databinding.FragmentStopwatchBinding
import com.example.studentapp.ui.classes.StopwatchAdapter


class StopwatchFragment : Fragment() {

    private var _binding: FragmentStopwatchBinding? = null
    private lateinit var adapter: StopwatchAdapter
    private lateinit var stopwatchViewModel : StopwatchViewModel

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStopwatchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // initialize viewModel
        stopwatchViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[StopwatchViewModel::class.java]

        // connect the button to its function
        binding.resetButtonStopwatch.setOnClickListener {stopwatchViewModel.reset()}

        // connect the text objects
        val textView: TextView = binding.textStopwatch
        val timeView: TextView = binding.timeStopwatch

        stopwatchViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        stopwatchViewModel.time.observe(viewLifecycleOwner) {
            timeView.text = it
        }

        // reset the viewModel any time today changes
        SharedData.today.observe(viewLifecycleOwner) {
            stopwatchViewModel.reset()
        }

        // gets adapter
        adapter = StopwatchAdapter ({item ->
            stopwatchViewModel.button(SharedData.switchClass(item))}, viewLifecycleOwner)

        binding.recyclerViewClasses.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewClasses.adapter = adapter

        // each time classList changes we call adapter.submitList
        SharedData.classList.observe(viewLifecycleOwner) { classList ->
            adapter.submitList(classList)
        }

        // each time currentClass changes, we call stopwatchViewModel.submitItem
        SharedData.currentClass.observe(viewLifecycleOwner) { item ->
            stopwatchViewModel.submitItem(item!!)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}