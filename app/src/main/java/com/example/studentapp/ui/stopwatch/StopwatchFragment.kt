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
import androidx.lifecycle.ViewModelProvider
import com.example.studentapp.databinding.FragmentStopwatchBinding
import java.util.Locale


class StopwatchFragment : Fragment() {

    private var _binding: FragmentStopwatchBinding? = null

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

        // connect the buttons to their functions
        binding.stopButton.setOnClickListener {stopwatchViewModel.stop()}
        binding.startButton.setOnClickListener {stopwatchViewModel.start()}
        binding.resetButton.setOnClickListener {stopwatchViewModel.reset()}

        // connect the text objects
        val textView: TextView = binding.textStopwatch
        val timeView: TextView = binding.timeView

        stopwatchViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        stopwatchViewModel.time.observe(viewLifecycleOwner) {
            timeView.text = it
        }

        return root;
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}