package com.hannah.studentapp.ui.stopwatch

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.registerReceiver
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hannah.studentapp.R
import com.hannah.studentapp.SharedData
import com.hannah.studentapp.databinding.FragmentStopwatchBinding
import com.hannah.studentapp.ui.classes.StopwatchAdapter
import com.hannah.studentapp.ui.classesItem.ClassesItem
import com.hannah.studentapp.ui.getThemeColor


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

    // handles the broadcast
    private val stopwatchReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "com.hannah.studentapp.STOPWATCH_UPDATE") {
                val seconds = intent.getIntExtra("seconds", 0)
                // Update ViewModel or UI
                Log.d("rst", "received")
                stopwatchViewModel.updateTimeFromService(seconds)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // initialize viewModel
        stopwatchViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[StopwatchViewModel::class.java]

        // connect the text objects
        val timeView: TextView = binding.timeStopwatch

        stopwatchViewModel.time.observe(viewLifecycleOwner) {
            timeView.text = it
        }

        // decide wether to lock or unlock the user
        stopwatchViewModel.running.observe(viewLifecycleOwner) {
            if (it) { // we are tracking -> lock user
                requireActivity().startLockTask()
                SharedData.locked = true
            } else { // we are not tracking anymore -> unlock user
                requireActivity().stopLockTask()
                SharedData.locked = false
            }
        }

        // gets adapter
        adapter = StopwatchAdapter (onStartClick = {item ->
            stopwatchViewModel.button(ClassesItem.switch(item))},
            lifecycleOwner = viewLifecycleOwner,
            onClassesItemClick = {item ->
                stopwatchViewModel.stop()

                // navigate s.t. we can still access stopwatch using bottom nav
                val navController = findNavController()
                val action = StopwatchFragmentDirections.actionStopwatchToClassesItem(item.getId())
                val navOptions = androidx.navigation.NavOptions.Builder()
                    .setPopUpTo(R.id.navigation_stopwatch, true) // keeps StopwatchFragment in back stack
                    .build()
                navController.navigate(action, navOptions)})

        binding.recyclerViewClasses.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewClasses.adapter = adapter

        // visually divide classes item with a vertical line
        val dividerItemDecoration = DividerItemDecoration(
            binding.recyclerViewClasses.context,
            DividerItemDecoration.VERTICAL
        )

        binding.recyclerViewClasses.addItemDecoration(dividerItemDecoration)
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
            val action = StopwatchFragmentDirections.actionStopwatchToInsights()

            val navOptions = androidx.navigation.NavOptions.Builder()
                .setPopUpTo(R.id.navigation_stopwatch, true) // keeps StopwatchFragment in back stack
                .build()
            navController.navigate(action, navOptions)
            // remove for live updates
            stopwatchViewModel.stop()
        }

        // load seconds
        stopwatchViewModel.load()

        // broadcast thingy
        val filter = IntentFilter("com.hannah.studentapp.STOPWATCH_UPDATE")
        registerReceiver(
            requireContext(),
            stopwatchReceiver,
            filter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    override fun onPause() {
        super.onPause()
        requireContext().unregisterReceiver(stopwatchReceiver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopwatchViewModel.stop()
        _binding = null
    }
}