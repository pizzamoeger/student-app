package com.example.studentapp.ui.calendar

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

class CalendarUtils {
    companion object {
        var selectedDate : LocalDate = LocalDate.now()
        var selectedTime : LocalTime = LocalTime.now()

        // returns month and year as string
        fun monthYearFromDate(date: LocalDate) : String {
            val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
            return date.format(formatter)
        }

        // returns week, month and year from date
        fun weekMonthYearFromDate(date: LocalDate) : String {
            val formatter = DateTimeFormatter.ofPattern("'Week' w',' MMMM yyyy")
            return date.format(formatter)
        }

        // returns time as string
        fun formattedTime(time : LocalTime) : String {
            val formatter = DateTimeFormatter.ofPattern("hh:mm:ss a")
            return time.format(formatter)
        }

        // returns time (shortened) as string
        fun formattedShortTime(time : LocalTime) : String {
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            return time.format(formatter)
        }

        // returns date as string
        fun formattedDate(date : LocalDate) : String {
            val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
            return date.format(formatter)
        }

        // returns days that should be visible in monthly calendar view
        fun daysForCalendarView(date: LocalDate) : List<LocalDate> {
            // get the first monday <= first day of month
            val firstOfMonth = date.withDayOfMonth(1)
            var dateCounter = firstOfMonth.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

            // get the 42 following days
            val daysList : MutableList<LocalDate> = mutableListOf()
            for (i in 1..42) {
                daysList.add(dateCounter)
                dateCounter = dateCounter.plusDays(1)
                // if we are in the next month and have a monday we can stop
                if (dateCounter.month == date.month.plus(1) && dateCounter.dayOfWeek == DayOfWeek.MONDAY) break
            }

            return daysList
        }
    }
}