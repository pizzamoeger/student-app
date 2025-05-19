package com.example.studentapp.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentapp.R
import com.example.studentapp.databinding.FragmentCalendarDailyBinding
import com.example.studentapp.ui.assignments.AssignmentsAdapter
import com.example.studentapp.ui.assignments.AssignmentsFragmentDirections
import com.example.studentapp.ui.assignments.assignment.Assignment
import com.example.studentapp.ui.calendar.CalendarUtils.Companion.selectedDate
import com.example.studentapp.ui.event.Event
import com.example.studentapp.ui.timetable.DayHourAdapter
import com.example.studentapp.ui.timetable.HourEvent
import java.time.LocalTime


class DailyCalendarFragment : Fragment() {

    private var _binding: FragmentCalendarDailyBinding? = null
    private val binding get() = _binding!!
    private lateinit var assignmentAdapter: AssignmentsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentCalendarDailyBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // bind button for next week
        binding.dayButtonLeft.setOnClickListener {
            previousDayAction()
        }

        // bind button for previous week
        binding.dayButtonRight.setOnClickListener {
            nextDayAction()
        }

        // bind button for new event
        binding.newEventButton.setOnClickListener {
            newEvent()
        }
    }

    private fun newEvent() {
        // navigate to event edit fragment
        val navController = findNavController()
        val action = DailyCalendarFragmentDirections.actionDayToEventEdit(id.toString())

        val navOptions = androidx.navigation.NavOptions.Builder()
            .setPopUpTo(R.id.fragment_event_edit, true) // keeps StopwatchFragment in back stack
            .build()
        navController.navigate(action, navOptions)
    }

    override fun onResume() {
        super.onResume()
        setDayView()
    }

    private fun setDayView() {
        // text that is displayed at top
        binding.dateTextView.text = CalendarUtils.formattedDate(selectedDate)

        // adapter that displays hours and events in that hour
        setHourAdapter()

        setAssignmentsAdapter()
    }

    private fun setHourAdapter() {
        // get adapter
        val hourAdapter = DayHourAdapter(requireContext(), hourEventList(),
            onCellEventClicked = {id ->
                val navController = findNavController()
                val action = DailyCalendarFragmentDirections.actionDayToEventEdit(id.toString())

                val navOptions = androidx.navigation.NavOptions.Builder()
                    .setPopUpTo(R.id.fragment_event_edit, true) // keeps StopwatchFragment in back stack
                    .build()
                navController.navigate(action, navOptions)}
        )
        binding.hourListView.adapter = hourAdapter
    }

    private fun setAssignmentsAdapter() {
        assignmentAdapter = AssignmentsAdapter ({
                item ->
            val action = DailyCalendarFragmentDirections.actionFragmentCalendarDayToFragmentEditAssignment(item.getId().toString())
            // if we move back to classes using the bottomnav, we want to go to classes
            val navOptions = androidx.navigation.NavOptions.Builder()
                .setPopUpTo(R.id.navigation_assignments, true) // keeps StopwatchFragment in back stack
                .build()
            findNavController().navigate(action, navOptions)
        },
            { item ->
                val action = DailyCalendarFragmentDirections.actionFragmentCalendarDayToNavigationAssignment(item.getId().toString())
                findNavController().navigate(action)},
            Assignment.getUncompletedDay(selectedDate))

        binding.assignmentsListView.layoutManager = LinearLayoutManager(requireContext())
        binding.assignmentsListView.adapter = assignmentAdapter

        // visually divide classes with a vertical line
        val dividerItemDecoration = DividerItemDecoration(
            binding.assignmentsListView.context,
            DividerItemDecoration.VERTICAL
        )

        binding.assignmentsListView.addItemDecoration(dividerItemDecoration)
    }

    // returns list of events for each hour in 0..23
    private fun hourEventList(): List<HourEvent> {
        val list: List<HourEvent> = List(
            size = 24,
            init = {hour ->
                val time = LocalTime.of(hour, 0)
                val events = mapOf(Pair("", Event.eventsForDateAndTimeDay(selectedDate, time)))
                HourEvent(time, events)
            }
        )
        return list
    }

    // what should happen when button for previous week is pressed
    private fun previousDayAction() {
        selectedDate = selectedDate.minusDays(1)
        setDayView()
    }

    // what should happen when button for next week is pressed
    private fun nextDayAction() {
        selectedDate = selectedDate.plusDays(1)
        setDayView()
    }
}