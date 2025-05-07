package com.example.studentapp.ui.stopwatch.insights

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.studentapp.SharedData
import com.example.studentapp.TimeInterval
import com.example.studentapp.ui.classesItem.ClassesItem
import com.example.studentapp.ui.getThemeColor
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class InsightsViewModel(app : Application) : AndroidViewModel(app) {
    private val _entries = MutableLiveData<List<PieEntry>>()
    val entries: LiveData<List<PieEntry>> = _entries

    private val handler = Handler(Looper.getMainLooper());

    init {
        runTimer()
    }

    fun runTimer() {
        handler.post(object : Runnable {
            override fun run() {
                // update what time(s) should display
                _entries.value = SharedData.classList.value?.map { classItem ->
                    PieEntry(classItem.getSeconds(TimeInterval.DAY).toFloat(), classItem.name) // Use actual values instead of 1f if you have them
                }

                // execute this every second
                handler.postDelayed(this, 1000)
            }
        })
    }
}