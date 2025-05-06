package com.example.studentapp.ui.stopwatch

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.studentapp.SharedData
import com.example.studentapp.ui.classesItem.ClassesItem
import java.time.LocalDate

class StopwatchViewModel(app : Application) : AndroidViewModel(app) {
    private val handler = Handler(Looper.getMainLooper());

    private var currentClass : ClassesItem? = null

    // is called each time the class in sharedViewModel changes
    fun submitItem(newClass: ClassesItem) {
        currentClass = newClass
    }

    private val _time = MutableLiveData<String>()
    val time: LiveData<String> = _time

    private var running = false;
    private var secondsTodayAll = 0
    private var secondsTotalAll = 0

    init {
        runTimer()
        // TODO change this so that time is reset at midnight every day
        // update date each time we switch to timer
        SharedData.updateDate()
        SharedData.classList.value!!.forEach { entry ->
            secondsTotalAll += entry.secondsTotal()
            secondsTodayAll += entry.secondsToday()
        }
    }

    fun runTimer() {
        handler.post(object : Runnable {
            override fun run() {
                // update what time(s) should display
                _time.value = ClassesItem.getTimeStringFromSeconds(secondsTodayAll)
                if (currentClass != null) currentClass!!.updateText()

                // if the stopwatch is running we increase seconds and save them
                if (running) {
                    secondsTodayAll++
                    secondsTotalAll++

                    if (currentClass != null) {
                        val today = SharedData.today.value.toString()
                        currentClass!!.studyTime[today] = currentClass!!.studyTime.getOrDefault(today, 0)+1
                    }

                    saveSeconds()
                }

                // execute this every second
                handler.postDelayed(this, 1000)
            }
        })
    }

    fun saveSeconds() {
        SharedData.save()
    }

    // stop tracking
    fun stop() {
        running = false
        if (SharedData.currentClass.value != null) SharedData.currentClass.value!!.updateTracking(false)
    }

    fun button(switched: Boolean = false) {
        // if switched is true, the class has switched
        if (switched) running = true
        else {
            running = !running
        }
    }

    // TODO is this actually still needed? rename maybe
    fun reset() {
        for (item in SharedData.classList.value!!) item.reset()
        saveSeconds()
    }
}