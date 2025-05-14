package com.example.studentapp.ui.calendar

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.studentapp.R
import com.example.studentapp.databinding.FragmentCalendarMonthlyBinding
import com.example.studentapp.ui.calendar.CalendarUtils.Companion.daysForCalendarView
import com.example.studentapp.ui.calendar.CalendarUtils.Companion.monthYearFromDate
import com.example.studentapp.ui.calendar.CalendarUtils.Companion.selectedDate


class MonthlyCalendarFragment : Fragment() {

    /*private var _binding: FragmentCalendarMonthlyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarMonthlyBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // bind button for previous month
        binding.monthButtonLeft.setOnClickListener {
            previousMonthAction()
        }

        // bind button for next month
        binding.monthButtonRight.setOnClickListener {
            nextMonthAction()
        }

        // TEMP
        // bind button for creating a new event
        binding.newEventButton.setOnClickListener {
            newEvent()
        }

        // visual divider between the day cells
        divider()
    }

    // TODO you could use a custom divider
    private fun divider() {
        // color of grid
        val drawable = ColorDrawable(android.R.attr.listDivider)

        // vertical lines
        val verticalLines = DividerItemDecoration(
            binding.calendarDayRecyclerView.context,
            DividerItemDecoration.VERTICAL
        )

        verticalLines.setDrawable(drawable)
        binding.calendarDayRecyclerView.addItemDecoration(verticalLines)

        // horizontal lines
        val horizontalLines = DividerItemDecoration(
            binding.calendarDayRecyclerView.context,
            DividerItemDecoration.HORIZONTAL
        )

        horizontalLines.setDrawable(drawable)
        binding.calendarDayRecyclerView.addItemDecoration(horizontalLines)
    }

    override fun onResume() {
        super.onResume()
        setMonthView()
    }

    private fun setMonthView() {
        // TODO make it so that you can press on this and then choose year and month
        // text that is displayed at top
        binding.monthYearTextView.text = monthYearFromDate(selectedDate)

        // days that should be displayed
        val daysList = daysForCalendarView(selectedDate)

        // adapter that displays days
        val calendarMonthAdapter = CalendarMonthAdapter(daysList=daysList,
            context=requireContext(),
            onItemClick = {
                day ->
                val message = ("Selected Date $day")
                selectedDate = day
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            })
        binding.calendarDayRecyclerView.adapter = calendarMonthAdapter

        // defines layout
        val layoutManager = GridLayoutManager(context, 7)
        binding.calendarDayRecyclerView.layoutManager = layoutManager
    }

    // is called on button press of create event
    private fun newEvent() {
        // navigate to event edit fragment
        val navController = findNavController()
        val action = MonthlyCalendarFragmentDirections.actionMonthToEventEdit(id.toString())

        val navOptions = androidx.navigation.NavOptions.Builder()
            .setPopUpTo(R.id.navigation_assignments, false) // keeps StopwatchFragment in back stack
            .build()
        navController.navigate(action, navOptions)
    }

    // what should happen when button for previous month is pressed
    private fun previousMonthAction() {
        selectedDate = selectedDate.minusMonths(1)
        setMonthView()
    }

    // what should happen when button for next month is pressed
    private fun nextMonthAction() {
        selectedDate = selectedDate.plusMonths(1)
        setMonthView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }*/
}