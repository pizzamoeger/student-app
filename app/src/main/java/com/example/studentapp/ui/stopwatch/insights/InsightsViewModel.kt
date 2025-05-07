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
    private val _entriesDay = MutableLiveData<List<PieEntry>>()
    val entriesDay: LiveData<List<PieEntry>> = _entriesDay

    private val _entriesWeek = MutableLiveData<List<PieEntry>>()
    val entriesWeek: LiveData<List<PieEntry>> = _entriesWeek

    private val _entriesMonth = MutableLiveData<List<PieEntry>>()
    val entriesMonth: LiveData<List<PieEntry>> = _entriesMonth

    private val handler = Handler(Looper.getMainLooper());

    init {
        runTimer()
    }

    fun runTimer() {
        handler.post(object : Runnable {
            override fun run() {
                // update what time(s) should display
                _entriesDay.value = SharedData.classList.value?.map { classItem ->
                    PieEntry(classItem.getSeconds(TimeInterval.DAY).toFloat(), classItem.name) // Use actual values instead of 1f if you have them
                }

                _entriesWeek.value = SharedData.classList.value?.map { classItem ->
                    PieEntry(classItem.getSeconds(TimeInterval.WEEK).toFloat(), classItem.name) // Use actual values instead of 1f if you have them
                }

                // update what time(s) should display
                _entriesMonth.value = SharedData.classList.value?.map { classItem ->
                    PieEntry(classItem.getSeconds(TimeInterval.MONTH).toFloat(), classItem.name) // Use actual values instead of 1f if you have them
                }

                // execute this every second
                handler.postDelayed(this, 1000)
            }
        })
    }
}