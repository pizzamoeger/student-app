package com.example.studentapp.ui.timetable

import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.studentapp.databinding.FragmentCalendarWeeklyBinding
import com.example.studentapp.ui.timetable.CalendarUtils.Companion.selectedDate
import java.time.LocalTime


class WeeklyCalendarFragment : Fragment() {

    private var _binding: FragmentCalendarWeeklyBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var monthDayText: TextView
    private lateinit var hourListView : ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_daily_calendar)
        initWidgets()
    }

    private fun initWidgets() {
        monthDayText = binding.monthYearTextViewWeek
        hourListView = binding.hourListView
    }

    override fun onResume() {
        super.onResume()
        setWeekView()
    }

    private fun setWeekView() {
        monthDayText.text = CalendarUtils.monthYearFromDate(selectedDate!!)
        setHourAdapter()
    }

    private fun setHourAdapter() {
        val hourAdapter = HourAdapter(requireContext(), hourEventList())
        hourListView.adapter = hourAdapter
    }

    private fun hourEventList(): List<HourEvent> {
        val list: MutableList<HourEvent> = MutableList<HourEvent>(
            size = 24,
            init = {hour ->
                val time = LocalTime.of(hour, 0)
                val events = Event.eventsForDateAndTime(selectedDate!!, time)
                HourEvent(time, events)
            }
        )
        return list
    }

    fun previousDayAction(view: View?) {
        selectedDate = selectedDate!!.minusWeeks(1)
        setWeekView()
    }

    fun nextDayAction(view: View?) {
        selectedDate = selectedDate!!.plusWeeks(1)
        setWeekView()
    }

    fun newEventAction(view: View?) {
        //startActivity(Intent(this, EventEditActivity::class.java))
    }
}