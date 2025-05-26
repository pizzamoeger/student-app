package com.example.studentapp.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.studentapp.R
import com.example.studentapp.databinding.FragmentCalendarWeeklyBinding
import com.example.studentapp.ui.calendar.CalendarUtils.Companion.selectedDate
import com.example.studentapp.ui.event.Event
import com.example.studentapp.ui.timetable.WeekHourAdapter
import com.example.studentapp.ui.timetable.HourEvent
import java.time.LocalTime


class WeeklyCalendarFragment : Fragment() {

    private var _binding: FragmentCalendarWeeklyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentCalendarWeeklyBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // bind button for next week
        binding.weekButtonLeft.setOnClickListener {
            previousWeekAction()
        }

        // bind button for previous week
        binding.weekButtonRight.setOnClickListener {
            nextWeekAction()
        }
    }

    override fun onResume() {
        super.onResume()
        setWeekView()
    }

    private fun setWeekView() {
        // text that is displayed at top
        binding.monthYearTextViewWeek.text = (context?.getString(R.string.week) ?: "Week") +CalendarUtils.weekMonthYearFromDate(selectedDate)

        // adapter that displays hours and events in that hour
        setHourAdapter()
    }

    private fun setHourAdapter() {
        // get adapter
        val hourAdapter = WeekHourAdapter(requireContext(), hourEventList(),
            onCellClicked = {date, time ->
                CalendarUtils.selectedDate = date
                CalendarUtils.selectedTime = time
                val navController = findNavController()
                val action = WeeklyCalendarFragmentDirections.actionWeekToEventEdit("-1")

                val navOptions = androidx.navigation.NavOptions.Builder()
                    .setPopUpTo(R.id.navigation_timetable, true) // keeps StopwatchFragment in back stack
                    .build()
                navController.navigate(action, navOptions)
            },
            onCellEventClicked = {date ->
                selectedDate = date
                val navController = findNavController()
                val action = WeeklyCalendarFragmentDirections.actionWeekToCalendarDay(selectedDate.toString())

                val navOptions = androidx.navigation.NavOptions.Builder()
                    .setPopUpTo(R.id.navigation_timetable, true) // keeps StopwatchFragment in back stack
                    .build()
                navController.navigate(action, navOptions)}
            )
        binding.hourListView.adapter = hourAdapter
    }

    // returns list of events for each hour in 7..19
    private fun hourEventList(): List<HourEvent> {
        val list: List<HourEvent> = List(
            size = 12,
            init = {hour ->
                val time = LocalTime.of(7+hour, 0)
                val events = Event.eventsForDateAndTimeWeek(selectedDate, time)
                HourEvent(time, events)
            }
        )
        return list
    }

    // what should happen when button for previous week is pressed
    private fun previousWeekAction() {
        selectedDate = selectedDate.minusWeeks(1)
        setWeekView()
    }

    // what should happen when button for next week is pressed
    private fun nextWeekAction() {
        selectedDate = selectedDate.plusWeeks(1)
        setWeekView()
    }
}