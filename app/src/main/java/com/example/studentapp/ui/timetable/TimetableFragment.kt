package com.example.studentapp.ui.timetable

import android.os.Bundle
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
import com.example.studentapp.databinding.FragmentTimetableBinding
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
        CalendarUtils.setMonthView(binding = binding.monthlyCalendarView, context=requireContext())

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewBinding = binding.monthlyCalendarView

        viewBinding.monthButtonLeft.setOnClickListener {
            CalendarUtils.selectedDate = CalendarUtils.selectedDate!!.minusMonths(1)
            CalendarUtils.setMonthView(binding = binding.monthlyCalendarView, context=requireContext())
        }

        viewBinding.monthButtonRight.setOnClickListener {
            CalendarUtils.selectedDate = CalendarUtils.selectedDate!!.plusMonths(1)
            CalendarUtils.setMonthView(binding = binding.monthlyCalendarView, context=requireContext())
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}