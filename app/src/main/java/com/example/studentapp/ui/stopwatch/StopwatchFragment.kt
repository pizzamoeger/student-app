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

    private var seconds = 0;
    private var running = false;
    private var wasRunning = false;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStopwatchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val stopButton: Button = root.findViewById(binding.stopButton.id)
        stopButton.setOnClickListener {onClickStop(root)}

        val startButton: Button = root.findViewById(binding.startButton.id)
        startButton.setOnClickListener {onClickStart(root)}

        val resetButton: Button = root.findViewById(binding.resetButton.id)
        resetButton.setOnClickListener {onClickReset(root)}
        // idk
        super.onCreate(savedInstanceState);
        // maybe: setContentView(R.layout.act)
        if (savedInstanceState != null) {
            seconds = savedInstanceState
                .getInt("seconds")
            running = savedInstanceState
                .getBoolean("running")
            wasRunning = savedInstanceState
                .getBoolean("wasRunning")
        }
        runTimer();
        return root;
    }

    override fun onSaveInstanceState(outState: Bundle) {
        //super.onSaveInstanceState(outState);
        outState.putInt("seconds", seconds);
        outState.putBoolean("running", running);
        outState.putBoolean("wasRunning", wasRunning);
    }

    override fun onPause() {
        super.onPause();
        wasRunning = running;
        running = false;
    }

    override fun onResume() {
        super.onResume();
        if (wasRunning) {
            running = true;
        }
    }

    fun onClickStart(view : View) {
        running = true;
    }

    fun onClickStop(view : View) {
        running = false;
    }

    fun onClickReset(view : View) {
        running = false;
        seconds = 0;
    }

    fun runTimer() {
        val stopwatchViewModel =
            ViewModelProvider(this).get(StopwatchViewModel::class.java)

        val textView: TextView = binding.textStopwatch
        val timeView: TextView = binding.timeView

        stopwatchViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val handler = Handler(Looper.getMainLooper());

        handler.post(object : Runnable {
            override fun run() {
                val hours = seconds / 3600
                val minutes = (seconds % 3600) / 60
                val secs = seconds % 60

                val time = String.format(
                    Locale.getDefault(),
                    "%d:%02d:%02d", hours,
                    minutes, secs
                )

                timeView.text = time

                if (running) {
                    seconds++
                }

                handler.postDelayed(this, 1000)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}