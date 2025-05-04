package com.example.studentapp.ui.stopwatch

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.studentapp.SharedData
import com.example.studentapp.ui.classes.ClassesItem
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class StopwatchViewModel(app : Application) : AndroidViewModel(app) {
    private val handler = Handler(Looper.getMainLooper());
    private val prefs = app.getSharedPreferences("stopwatch_prefs", Context.MODE_PRIVATE)

    private var currentClass : ClassesItem? = null

    fun submitItem(newClass: ClassesItem) {
        // is called each time the class in sharedViewModel changes
        currentClass = newClass
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is Stopwatch Fragment"
    }
    private val _time = MutableLiveData<String>()

    val text: LiveData<String> = _text
    val time: LiveData<String> = _time

    private var secondsToday = prefs.getInt("seconds_today", 0);
    private var secondsTotal = prefs.getInt("seconds_total", 0);
    private var running = false;

    init {
        runTimer()

        // Checks if app was already opened today
        // if not, time is reset
        // TODO change this so that time is reset at midnight every day
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
                _time.value = ClassesItem.getTimeStringFromSeconds(secondsToday)
                if (currentClass != null) currentClass!!.update_text()

                // if the stopwatch is running we increase seconds and save them
                if (running) {
                    secondsToday++
                    secondsTotal++

                    if (currentClass != null) {
                        currentClass!!.secondsToday++
                        currentClass!!.secondsTotal++
                    }

                    saveSeconds()
                }

                // execute this every second
                handler.postDelayed(this, 1000)
            }
        })
    }

    fun saveSeconds() {
        prefs.edit().putInt("seconds_today", secondsToday).apply()
        prefs.edit().putInt("seconds_total", secondsTotal).apply()
        SharedData.save()
    }

    fun button(switched: Boolean = false) {
        // if switched is true, the class has switched
        if (switched) running = true
        else {
            running = !running
        }
    }

    fun reset() {
        secondsToday = 0;
        for (item in SharedData.classList.value!!) item.reset()
        saveSeconds()
    }
}