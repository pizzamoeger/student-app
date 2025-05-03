package com.example.studentapp.ui.stopwatch

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Locale

class StopwatchViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Stopwatch Fragment"
    }
    private val _time = MutableLiveData<String>()

    val text: LiveData<String> = _text
    val time: LiveData<String> = _time

    private var seconds = 0;
    private var running = false;
    private var wasRunning = false;

    private val handler = Handler(Looper.getMainLooper());

    init {
        runTimer()
    }

    fun runTimer() {
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

                _time.value = time

                if (running) {
                    seconds++
                }

                handler.postDelayed(this, 1000)
            }
        })
    }

    /*override fun onPause() {
        super.onPause();
        wasRunning = running;
        running = false;
    }

    override fun onResume() {
        super.onResume();
        if (wasRunning) {
            running = true;
        }
    }*/

    fun start(view : View) {
        running = true;
    }

    fun stop(view : View) {
        running = false;
    }

    fun reset(view : View) {
        running = false;
        seconds = 0;
    }
}