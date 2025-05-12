package com.example.studentapp.ui.classesItem

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.studentapp.SharedData
import com.example.studentapp.TimeInterval
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.util.Locale

data class ClassesItem(
    val id: Int,
    val name: String,
    val studyTime: MutableMap<String, Int>,
    val color : Int
) {
    override fun toString(): String {
        return name
    }
    // static
    companion object {
        fun getTimeStringFromSeconds(secs: Int): String {
            // calculate hours, minutes and seconds and update _time accordingly
            val hours = secs / 3600
            val minutes = (secs % 3600) / 60
            val seconds = secs % 60

            val time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds)
            return time
        }
    }

    // get seconds spent in timeframe
    private fun secondsInInterval(from : String, to : String) : Int {
        var seconds = 0;
        studyTime.forEach { entry ->
            if (entry.key in from..to) {
                seconds += entry.value
            }
        }
        return seconds
    }

    // returns seconds in timespan depending on type
    fun getSeconds(type : TimeInterval = TimeInterval.DEFAULT, from: String = "", to: String = "") : Int{
        if (type == TimeInterval.DAY) return secondsToday()
        if (type == TimeInterval.WEEK) return secondsThisWeek()
        if (type == TimeInterval.MONTH) return secondsThisMonth()
        if (type == TimeInterval.TOTAL) return secondsTotal()
        if (from == "" || to == "") {
            Log.e("GET SECONDS", "called ClassesItem.getSeconds with DEFAULT but no from or to")
            return 0
        }
        return secondsInInterval(from, to)
    }

    // get seconds spent studying today
    private fun secondsToday() : Int {
        SharedData.updateDate()
        val today = SharedData.today.value
        return secondsInInterval(today.toString(), today.toString())
    }

    // get seconds spent studying this week
    private fun secondsThisWeek() : Int {
        SharedData.updateDate()
        val today : LocalDate? = SharedData.today.value
        // TODO be able to choose when week starts
        val monday = today!!.with(TemporalAdjusters.previous(DayOfWeek.MONDAY))
        return secondsInInterval(monday.toString(), today.toString())
    }

    // get seconds spent studying this month
    private fun secondsThisMonth() : Int {
        SharedData.updateDate()
        val today : LocalDate? = SharedData.today.value
        val firstOfMonth = LocalDate.of(today!!.year, today.month, 1)
        return secondsInInterval(firstOfMonth.toString(), today.toString())
    }

    // get total seconds spent studying
    private fun secondsTotal() : Int {
        SharedData.updateDate()
        val today : LocalDate? = SharedData.today.value
        return secondsInInterval(LocalDate.MIN.toString(), today.toString())
    }

    // string of secondsToday
    private val _text = MutableLiveData<String>().apply {
        value = getTimeStringFromSeconds(secondsToday())
    }
    val text : LiveData<String> = _text

    // if this class is currently being tracked
    private val _tracking = MutableLiveData<Boolean>().apply {
        value = false
    }
    val tracking : LiveData<Boolean> = _tracking

    // update string for secondsToday
    fun updateText() {
        _text.value = getTimeStringFromSeconds(secondsToday())
    }

    // update if this class is being tracked
    fun updateTracking(to : Boolean) {
        _tracking.value = to
        // TODO bug: total time is more than time of classes added
    }

    // reset secondsToday
    fun reset() {
        updateText()
    }
}