package com.example.studentapp.ui.stopwatch

import android.R
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentapp.SharedViewModel
import com.example.studentapp.databinding.FragmentStopwatchBinding
import com.example.studentapp.ui.classes.ClassesAdapter
import com.example.studentapp.ui.classes.ClassesItem
import com.example.studentapp.ui.classes.StopwatchAdapter
import java.util.Locale


class StopwatchFragment : Fragment() {

    private var _binding: FragmentStopwatchBinding? = null
    private lateinit var adapter: StopwatchAdapter
    private val sharedViewModel : SharedViewModel by activityViewModels()
    //private val stopwatchViewModel : StopwatchViewModel()

    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStopwatchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val stopwatchViewModel = ViewModelProvider(
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

        return root;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val stopwatchViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[StopwatchViewModel::class.java]

        // gets adapter
        adapter = StopwatchAdapter ({item ->
            stopwatchViewModel.button(sharedViewModel.switchClass(item))}, viewLifecycleOwner)

        binding.recyclerViewClasses.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewClasses.adapter = adapter

        // each time classList changes we call adapter.submitList
        sharedViewModel.classList.observe(viewLifecycleOwner) { classList ->
            adapter.submitList(classList)
        }

        // each time currentClass changes, we call stopwatchViewModel.submitItem
        sharedViewModel.currentClass.observe(viewLifecycleOwner) { item ->
            stopwatchViewModel.submitItem(item!!)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}