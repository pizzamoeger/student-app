package com.example.studentapp.ui.stopwatch

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.studentapp.SharedData
import com.example.studentapp.TimeInterval
import com.example.studentapp.ui.classesItem.ClassesItem

class StopwatchViewModel(app : Application) : AndroidViewModel(app) {
    private val handler = Handler(Looper.getMainLooper())

    private val _time = MutableLiveData<String>()
    val time: LiveData<String> = _time

    private var _running = MutableLiveData(false)
    val running: LiveData<Boolean> = _running
    private var secondsTodayAll = 0
    private var secondsTotalAll = 0

    init {
        runTimer()
        // TODO change this so that time is reset at midnight every day
        // update date each time we switch to timer
        SharedData.updateDate()
        load()
    }

    private fun runTimer() {
        handler.post(object : Runnable {
            override fun run() {
                // update what time(s) should display
                _time.value = ClassesItem.getTimeStringFromSeconds(secondsTodayAll)
                ClassesItem.getCurrent().updateText()

                // if the stopwatch is running we increase seconds and save them
                if (_running.value!!) {
                    secondsTodayAll++
                    secondsTotalAll++

                    val today = SharedData.today.value.toString()
                    ClassesItem.getCurrent().studyTime[today] = ClassesItem.getCurrent().studyTime.getOrDefault(today, 0)+1 // todo private

                    saveSeconds()
                }

                // execute this every second
                handler.postDelayed(this, 1000)
            }
        })
    }

    fun saveSeconds() {
        ClassesItem.save()
    }

    // stop tracking
    fun stop() {
        _running.value = false
        if (ClassesItem.getCurrent() != ClassesItem()) ClassesItem.getCurrent().updateTracking(false)
    }

    // button functionality
    fun button(switched: Boolean = false) {
        // if switched is true, the class has switched
        if (switched) _running.value = true
        else {
            _running.value= !(_running.value!!)
        }
    }

    // loads secondsTotalAll and secondsTodayAll
    fun load() {
        secondsTotalAll = 0
        secondsTodayAll = 0
        ClassesItem.getList().forEach { entry ->
            secondsTotalAll += entry.getSeconds(TimeInterval.TOTAL)
            secondsTodayAll += entry.getSeconds(TimeInterval.DAY)
        }
        _time.value = ClassesItem.getTimeStringFromSeconds(secondsTodayAll)
    }

    // TODO is this actually still needed? rename maybe
    fun reset() {
        for (item in ClassesItem.getList()) item.reset()
        saveSeconds()
    }
}