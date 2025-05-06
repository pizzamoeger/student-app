package com.example.studentapp.ui.stopwatch

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentapp.MainActivity
import com.example.studentapp.R
import com.example.studentapp.SharedData
import com.example.studentapp.databinding.FragmentStopwatchBinding
import com.example.studentapp.ui.classes.StopwatchAdapter
import com.example.studentapp.ui.getThemeColor
import com.example.studentapp.ui.stopwatch.insights.InsightsFragmentDirections


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
        val timeView: TextView = binding.timeStopwatch

        stopwatchViewModel.time.observe(viewLifecycleOwner) {
            timeView.text = it
        }

        // reset the viewModel any time today changes
        SharedData.today.observe(viewLifecycleOwner) {
            stopwatchViewModel.reset()
        }

        // gets adapter
        adapter = StopwatchAdapter (onStartClick = {item ->
            stopwatchViewModel.button(SharedData.switchClass(item))},
            lifecycleOwner = viewLifecycleOwner,
            onClassesItemClick = {item ->
                val action = StopwatchFragmentDirections.actionStopwatchToClassesItem(item.id.toString())
                findNavController().navigate(action)
                stopwatchViewModel.stop()})

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

    override fun onResume() {
        super.onResume()

        // TODO make function for this
        // display top navigation
        val activityBinding = (requireActivity() as MainActivity).binding
        val toolbar = activityBinding.includedToolbar

        // left selection links to nothing (is current)
        context?.let { toolbar.textLeft.setTextColor(it.getThemeColor(android.R.attr.textColorSecondary)) }
        context?.let { toolbar.lineLeft.setBackgroundColor(it.getThemeColor(android.R.attr.textColorSecondary)) }
        toolbar.selectionLeft.setOnClickListener {}

        // right selection links to insight
        context?.let { toolbar.textRight.setTextColor(it.getThemeColor(android.R.attr.textColorTertiary)) }
        context?.let { toolbar.lineRight.setBackgroundColor(it.getThemeColor(android.R.attr.textColorTertiary)) }
        toolbar.selectionRight.setOnClickListener {
            val action = StopwatchFragmentDirections.actionStopwatchToInsights()
            findNavController().navigate(action)
            stopwatchViewModel.stop()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}