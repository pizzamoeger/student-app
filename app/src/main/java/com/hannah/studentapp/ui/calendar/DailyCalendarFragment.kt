package com.hannah.studentapp.ui.calendar

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hannah.studentapp.R
import com.hannah.studentapp.databinding.FragmentCalendarDailyBinding
import com.hannah.studentapp.ui.assignments.AssignmentsAdapter
import com.hannah.studentapp.ui.assignments.assignment.Assignment
import com.hannah.studentapp.ui.assignments.assignment.EditAssignmentFragmentDirections
import com.hannah.studentapp.ui.calendar.CalendarUtils.Companion.selectedDate
import com.hannah.studentapp.ui.event.Event
import com.hannah.studentapp.ui.timetable.DayHourAdapter
import com.hannah.studentapp.ui.timetable.HourEvent
import java.time.LocalDate
import java.time.LocalTime


class DailyCalendarFragment : Fragment() {

    private var _binding: FragmentCalendarDailyBinding? = null
    private val binding get() = _binding!!
    private lateinit var assignmentAdapter: AssignmentsAdapter
    private val args: DailyCalendarFragmentArgs by navArgs()
    var fromTimeTable = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentCalendarDailyBinding.inflate(inflater, container, false)
        val args: DailyCalendarFragmentArgs by navArgs()
        selectedDate = LocalDate.parse(args.date)
        fromTimeTable = args.fromTimetable
        val root: View = binding.root
        return root
    }

    // ToDO back

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
        val action = DailyCalendarFragmentDirections.actionDayToEventEdit(id, false)

        val navOptions = androidx.navigation.NavOptions.Builder()
            .setPopUpTo(R.id.navigation_timetable, true) // keeps StopwatchFragment in back stack
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
                val action = DailyCalendarFragmentDirections.actionDayToEventEdit(id, false)

                val navOptions = androidx.navigation.NavOptions.Builder()
                    .setPopUpTo(R.id.navigation_timetable, true) // keeps StopwatchFragment in back stack
                    .build()
                navController.navigate(action, navOptions)}
        )
        binding.hourListView.adapter = hourAdapter
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        //enable menu
        setHasOptionsMenu(true)

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(this){
                //true means that the callback is enabled
                this.isEnabled = true
                if (fromTimeTable) {
                    val navController = findNavController()
                    val action = DailyCalendarFragmentDirections.actionFragmentCalendarDayToNavigationTimetable()
                    navController.navigate(action)
                } else {
                    val navController = findNavController()
                    val action = DailyCalendarFragmentDirections.actionFragmentCalendarDayToFragmentMonthView()
                    navController.navigate(action)
                }
                //exitDialog() //dialog to conform exit
            }
    }

    private fun setAssignmentsAdapter() {
        assignmentAdapter = AssignmentsAdapter ({
                item ->
            val action = DailyCalendarFragmentDirections.actionFragmentCalendarDayToFragmentEditAssignment(item.getId())
            // if we move back to classes using the bottomnav, we want to go to classes
            var id = R.id.navigation_assignments
            if (fromTimeTable) id = R.id.navigation_timetable
            val navOptions = androidx.navigation.NavOptions.Builder()
                .setPopUpTo(id, true) // keeps StopwatchFragment in back stack
                .build()
            findNavController().navigate(action, navOptions)
        },
            { item ->
                // todo do au denke ew
                val action = DailyCalendarFragmentDirections.actionFragmentCalendarDayToNavigationAssignment(item.getId(), false)
                var id = R.id.navigation_assignments
                if (fromTimeTable) id = R.id.navigation_timetable
                val navOptions = androidx.navigation.NavOptions.Builder()
                    .setPopUpTo(id, true) // keeps StopwatchFragment in back stack
                    .build()
                findNavController().navigate(action,  navOptions)},
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