package com.example.studentapp.ui.timetable

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.studentapp.SharedData
import com.example.studentapp.databinding.FragmentCalendarMonthlyBinding
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class CalendarUtils {
    companion object {
        var selectedDate : LocalDate? = SharedData.today.value

        fun monthYearFromDate(date: LocalDate) : String {
            val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
            return date.format(formatter)
        }

        fun formattedShortTime(time : LocalTime) : String {
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            return time.format(formatter)
        }

        fun daysInMonth(date: LocalDate) : Pair<List<String>, List<Boolean>> {
            val yearMonth = YearMonth.from(date)
            val daysInMonthCount = yearMonth.lengthOfMonth()
            val firstDayOfMonth = selectedDate!!.withDayOfMonth(1)
            val dayOfWeek = firstDayOfMonth.dayOfWeek.value

            val daysInPreviousMonth = YearMonth.from(date.minusMonths(1)).lengthOfMonth()

            // TODO make it not always 42: if it starts on monday, we can remove the first 7 days
            val daysInMonthArrayText = MutableList<String>(42, { _-> 0.toString() })
            val daysInMonthArrayBoolean = MutableList<Boolean>(42, { _-> false })

            for (i in 1..42) {
                if (i <= dayOfWeek || i > daysInMonthCount + dayOfWeek) {
                    daysInMonthArrayBoolean[i-1] = false
                    if (i <= dayOfWeek) {
                        daysInMonthArrayText[i-1] = (daysInPreviousMonth-dayOfWeek+i).toString()
                    }
                    else {
                        daysInMonthArrayText[i-1] = (i-(daysInMonthCount + dayOfWeek)).toString()
                    }
                } else {
                    daysInMonthArrayText[i-1] = (i - dayOfWeek).toString()
                    daysInMonthArrayBoolean[i-1] = true
                }
            }
            return Pair(daysInMonthArrayText, daysInMonthArrayBoolean)
        }
    }
}