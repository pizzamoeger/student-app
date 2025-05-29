package com.hannah.studentapp.ui.stopwatch

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hannah.studentapp.TimeInterval
import com.hannah.studentapp.ui.classesItem.ClassesItem

class StopwatchViewModel(app : Application) : AndroidViewModel(app) {
    private val handler = Handler(Looper.getMainLooper())

    private val _time = MutableLiveData<String>()
    val time: LiveData<String> = _time

    private var _running = MutableLiveData(false)
    val running: LiveData<Boolean> = _running
    private var secondsTodayAll = 0
    private var secondsTotalAll = 0

    init {
        load()
    }

    private fun runTimer() {
        // start timer
        val context = getApplication<Application>()
        val intent = Intent(context, StopwatchService::class.java)
        context.startForegroundService(intent)
    }

    fun updateTimeFromService(seconds : Int) {
        ClassesItem.getCurrent().addSecond(getApplication())

        load()

        // update what time(s) should display
        _time.value = ClassesItem.getTimeStringFromSeconds(secondsTodayAll)
        for (classItem in ClassesItem.getList()) classItem.updateText()
    }

    // stop tracking
    fun stop() {
        _running.value = false
        if (ClassesItem.getCurrent() != ClassesItem()) ClassesItem.getCurrent().updateTracking(false)
        // stop
        val context = getApplication<Application>()
        val intent = Intent(context, StopwatchService::class.java)
        context.stopService(intent)
    }

    // button functionality
    fun button(switched: Boolean = false) {
        // if switched is true, the class has switched
        if (switched) _running.value = true
        else {
            _running.value= !(_running.value!!)
        }
        if (_running.value == true) runTimer()
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
}