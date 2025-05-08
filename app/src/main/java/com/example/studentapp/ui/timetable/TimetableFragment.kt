package com.example.studentapp.ui.timetable

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.studentapp.MainActivity
import com.example.studentapp.SharedData
import com.example.studentapp.databinding.FragmentCalendarMonthlyBinding
import com.example.studentapp.databinding.FragmentTimetableBinding
import com.example.studentapp.ui.timetable.CalendarUtils.Companion.daysInMonth
import com.example.studentapp.ui.timetable.CalendarUtils.Companion.monthYearFromDate
import com.example.studentapp.ui.timetable.CalendarUtils.Companion.selectedDate
import com.github.mikephil.charting.utils.Utils
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter


class TimetableFragment : Fragment() {

    private var _binding: FragmentTimetableBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val timetableViewModel =
            ViewModelProvider(this).get(TimetableViewModel::class.java)

        _binding = FragmentTimetableBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*timetableViewModel.text.observe(viewLifecycleOwner) {
            monthYearText.text = it
        }*/
        setMonthView()

        return root
    }

    fun setMonthView() {
        binding.monthlyCalendarView.monthYearTextView.text = monthYearFromDate(selectedDate!!)
        val (daysInMonthText, daysInMonthSelected) = daysInMonth(selectedDate!!)

        Log.d("rr", "here")
        val calendarAdapter = CalendarAdapter(daysOfMonthText=daysInMonthText,
            daysOfMonthSelected=daysInMonthSelected,
            context=requireContext(),
            onItemListener = {
                    position, dayText ->
                if (dayText != "") {
                    val message =
                        ("Selected Date $dayText").toString() + " " + monthYearFromDate(selectedDate!!)
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
            })
        val layoutManager = GridLayoutManager(context, 7)
        binding.monthlyCalendarView.calendarDayRecyclerView.layoutManager = layoutManager
        binding.monthlyCalendarView.calendarDayRecyclerView.adapter = calendarAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewBinding = binding.monthlyCalendarView

        viewBinding.monthButtonLeft.setOnClickListener {
            CalendarUtils.selectedDate = CalendarUtils.selectedDate!!.minusMonths(1)
            setMonthView()
        }

        viewBinding.monthButtonRight.setOnClickListener {
            CalendarUtils.selectedDate = CalendarUtils.selectedDate!!.plusMonths(1)
            setMonthView()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}