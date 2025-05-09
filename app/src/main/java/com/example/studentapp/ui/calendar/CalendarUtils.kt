package com.example.studentapp.ui.calendar

import com.example.studentapp.SharedData
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

        fun formattedTime(time : LocalTime) : String {
            val formatter = DateTimeFormatter.ofPattern("hh:mm:ss a")
            return time.format(formatter)
        }

        fun formattedShortTime(time : LocalTime) : String {
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            return time.format(formatter)
        }

        fun formattedDate(date : LocalDate) : String {
            val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
            return date.format(formatter)
        }

        fun daysInMonth(date: LocalDate) : Pair<List<LocalDate>, List<Boolean>> {
            val yearMonth = YearMonth.from(date)
            val daysInMonthCount = yearMonth.lengthOfMonth()
            val firstDayOfMonth = selectedDate!!.withDayOfMonth(1)
            val dayOfWeek = firstDayOfMonth.dayOfWeek.value

            val daysInPreviousMonth = YearMonth.from(date.minusMonths(1)).lengthOfMonth()

            // TODO make it not always 42: if it starts on monday, we can remove the first 7 days
            val daysInMonthArrayText = MutableList<LocalDate>(42, { _-> selectedDate!! })
            val daysInMonthArrayBoolean = MutableList<Boolean>(42, { _-> false })

            for (i in 1..42) {
                if (i <= dayOfWeek || i > daysInMonthCount + dayOfWeek) {
                    daysInMonthArrayBoolean[i-1] = false
                    if (i <= dayOfWeek) {
                        val lastMonth = LocalDate.of(selectedDate!!.year, selectedDate!!.month, 1).minusMonths(1)
                        daysInMonthArrayText[i-1] = LocalDate.of(lastMonth.year, lastMonth.month, daysInPreviousMonth-dayOfWeek+i)
                    }
                    else {
                        val lastMonth = LocalDate.of(selectedDate!!.year, selectedDate!!.month, 1).plusMonths(1)
                        daysInMonthArrayText[i-1] = LocalDate.of(lastMonth.year, lastMonth.month, i-(daysInMonthCount + dayOfWeek))
                    }
                } else {
                    daysInMonthArrayText[i-1] = LocalDate.of(selectedDate!!.year, selectedDate!!.month, i-dayOfWeek)
                    daysInMonthArrayBoolean[i-1] = true
                }
            }
            return Pair(daysInMonthArrayText, daysInMonthArrayBoolean)
        }
    }
}