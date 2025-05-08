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
    private var selectedDate = SharedData.today.value!!

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.monthButtonLeft.setOnClickListener {
            selectedDate = selectedDate.minusMonths(1)
            setMonthView()
        }

        binding.monthButtonRight.setOnClickListener {
            selectedDate = selectedDate.plusMonths(1)
            setMonthView()
        }
    }

    private fun setMonthView() {
        binding.monthYearTextView.text = monthYearFromDate(selectedDate)
        val daysInMonth = daysInMonth(selectedDate)

        val calendarAdapter = CalendarAdapter(daysInMonth, {
            position, dayText ->
            if (!dayText.equals("")) {
                val message =
                    ("Selected Date $dayText").toString() + " " + monthYearFromDate(selectedDate)
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        })
        val layoutManager = GridLayoutManager(context, 7)
        binding.calendarDayRecyclerView.layoutManager = layoutManager
        binding.calendarDayRecyclerView.adapter = calendarAdapter
    }


    private fun monthYearFromDate(date: LocalDate) : String {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date.format(formatter)
    }

    private fun daysInMonth(date: LocalDate) : List<String> {
        val yearMonth = YearMonth.from(date)
        val daysInMonthCount = yearMonth.lengthOfMonth()
        val firstDayOfMonth = selectedDate.withDayOfMonth(1)
        val dayOfWeek = firstDayOfMonth.dayOfWeek.value

        val daysInMonthArray = MutableList<String>(42, { _-> 0.toString() })

        for (i in 1..42) {
            if (i <= dayOfWeek || i > daysInMonthCount + dayOfWeek) {
                daysInMonthArray[i-1] = ""
            } else {
                daysInMonthArray[i-1] = (i - dayOfWeek).toString()
            }
        }
        return daysInMonthArray
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}