package com.example.studentapp.ui.classes

import java.util.Locale

data class ClassesItem(
    val id: Int,
    val name: String,
    var secondsToday: Int,
    var secondsTotal: Int) {
    companion object {
        fun getTimeStringFromSeconds(secs: Int): String {
            // calculate hours, minutes and seconds and update _time accordingly
            val hours = secs / 3600
            val minutes = (secs % 3600) / 60
            val secs = secs % 60

            val time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs)
            return time
        }
    }
}