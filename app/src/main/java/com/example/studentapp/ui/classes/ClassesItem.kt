package com.example.studentapp.ui.classes

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.studentapp.SharedData
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.util.Locale

data class ClassesItem(
    val id: Int,
    val name: String,
    val studyTime: MutableMap<String, Int>) {

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
    fun getSeconds(from : String, to : String) : Int {
        var seconds = 0;
        studyTime.forEach { entry ->
            if (entry.key in from..to) {
                seconds += entry.value
            }
        }
        return seconds
    }

    // get seconds spent studying today
    fun secondsToday() : Int {
        SharedData.updateDate()
        val today = SharedData.today.value
        return getSeconds(today.toString(), today.toString())
    }

    // get seconds spent studying this week
    fun secondsThisWeek() : Int {
        SharedData.updateDate()
        val today : LocalDate? = SharedData.today.value
        // TODO be able to choose when week starts
        val monday = today!!.with(TemporalAdjusters.previous(DayOfWeek.MONDAY))
        return getSeconds(monday.toString(), today.toString())
    }

    // get seconds spent studying this month
    fun secondsThisMonth() : Int {
        SharedData.updateDate()
        val today : LocalDate? = SharedData.today.value
        val firstOfMonth = LocalDate.of(today!!.year, today.month, 1)
        return getSeconds(firstOfMonth.toString(), today.toString())
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
    }

    // reset secondsToday
    fun reset() {
        // secondsToday = 0
        updateText()
    }
}