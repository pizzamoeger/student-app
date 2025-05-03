package com.example.studentapp.ui.stopwatch

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
class StopwatchViewModel(app : Application) : AndroidViewModel(app) {
    private val handler = Handler(Looper.getMainLooper());
    private val prefs = app.getSharedPreferences("stopwatch_prefs", Context.MODE_PRIVATE)

    private val _text = MutableLiveData<String>().apply {
        value = "This is Stopwatch Fragment"
    }
    private val _time = MutableLiveData<String>()

    val text: LiveData<String> = _text
    val time: LiveData<String> = _time

    private var seconds = prefs.getInt("seconds", 0);
    private var running = false;
    private var wasRunning = false;

    init {
        runTimer()

        val lastOpened = prefs.getString("last_opened", null)
        val today = LocalDate.now().toString()

        if (lastOpened != today) {
            reset()
            prefs.edit().putString("last_opened", today).apply()
        }
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
                    saveSeconds()
                }

                handler.postDelayed(this, 1000)
            }
        })
    }

    fun saveSeconds() {
        prefs.edit().putInt("seconds", seconds).apply()
    }

    fun start() {
        running = true;
    }

    fun stop() {
        running = false;
    }

    fun reset() {
        running = false;
        seconds = 0;
    }
}