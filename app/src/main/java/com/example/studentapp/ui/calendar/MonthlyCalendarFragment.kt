package com.example.studentapp.ui.calendar

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.studentapp.R
import com.example.studentapp.databinding.FragmentCalendarMonthlyBinding
import com.example.studentapp.ui.getThemeColor
import com.example.studentapp.ui.calendar.CalendarUtils.Companion.daysInMonth
import com.example.studentapp.ui.calendar.CalendarUtils.Companion.monthYearFromDate
import com.example.studentapp.ui.calendar.CalendarUtils.Companion.selectedDate


class MonthlyCalendarFragment : Fragment() {

    private var _binding: FragmentCalendarMonthlyBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarMonthlyBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setMonthView()

        return root
    }

    fun setMonthView() {
        binding.monthYearTextView.text = monthYearFromDate(selectedDate!!)
        val (daysInMonthText, daysInMonthSelected) = daysInMonth(selectedDate!!)

        val calendarAdapter = CalendarAdapter(daysOfMonthText=daysInMonthText,
            daysOfMonthSelected=daysInMonthSelected,
            context=requireContext(),
            onItemListener = {
                    day ->
                val message =
                    ("Selected Date ${day.toString()}")
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            })
        val layoutManager = GridLayoutManager(context, 7)
        binding.calendarDayRecyclerView.layoutManager = layoutManager
        binding.calendarDayRecyclerView.adapter = calendarAdapter
    }

    override fun onResume() {
        super.onResume()
        setMonthView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.monthButtonLeft.setOnClickListener {
            CalendarUtils.selectedDate = CalendarUtils.selectedDate!!.minusMonths(1)
            setMonthView()
        }

        binding.monthButtonRight.setOnClickListener {
            CalendarUtils.selectedDate = CalendarUtils.selectedDate!!.plusMonths(1)
            setMonthView()
        }

        binding.newEventButton.setOnClickListener {
            newEvent()
        }

        // TODO make a custom for this
        val dividerItemDecoration = DividerItemDecoration(
            binding.calendarDayRecyclerView.context,
            DividerItemDecoration.VERTICAL
        )

        val drawable = ColorDrawable(binding.calendarDayRecyclerView.context.getThemeColor(androidx.appcompat.R.attr.colorPrimary))
        dividerItemDecoration.setDrawable(drawable)
        val divider2 = DividerItemDecoration(
            binding.calendarDayRecyclerView.context,
            DividerItemDecoration.HORIZONTAL
        )

        divider2.setDrawable(drawable)

        binding.calendarDayRecyclerView.addItemDecoration(dividerItemDecoration)
        binding.calendarDayRecyclerView.addItemDecoration(divider2)
    }

    fun newEvent() {

        //val action = MonthlyCalendarFragmentDirections.actionNotsToEventEdit()
        //findNavController().navigate(action)
        val navController = requireActivity().findNavController(R.id.nav_host_fragment_activity_main) // or your NavHost ID
        navController.navigate(R.id.fragment_event_edit)
        //val navController = findNavController()
        //navController.navigate(R.id.action_nots_to_event_edit)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}