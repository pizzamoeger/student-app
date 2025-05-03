package com.example.studentapp.ui.stopwatch

import android.R
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.studentapp.databinding.FragmentStopwatchBinding
import java.util.Locale


class StopwatchFragment : Fragment() {

    private var _binding: FragmentStopwatchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStopwatchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val stopwatchViewModel =
            ViewModelProvider(this).get(StopwatchViewModel::class.java)

        val stopButton: Button = root.findViewById(binding.stopButton.id)
        stopButton.setOnClickListener {stopwatchViewModel.onClickStop(root)}

        val startButton: Button = root.findViewById(binding.startButton.id)
        startButton.setOnClickListener {stopwatchViewModel.onClickStart(root)}

        val resetButton: Button = root.findViewById(binding.resetButton.id)
        resetButton.setOnClickListener {stopwatchViewModel.onClickReset(root)}

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

    /*override fun onSaveInstanceState(outState: Bundle) {
        //super.onSaveInstanceState(outState);
        outState.putInt("seconds", seconds);
        outState.putBoolean("running", running);
        outState.putBoolean("wasRunning", wasRunning);
    }*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}