package com.example.studentapp.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.studentapp.databinding.FragmentCalendarWeeklyBinding
import com.example.studentapp.ui.calendar.CalendarUtils.Companion.selectedDate
import com.example.studentapp.ui.timetable.Event
import com.example.studentapp.ui.timetable.HourAdapter
import com.example.studentapp.ui.timetable.HourEvent
import java.time.LocalTime


class WeeklyCalendarFragment : Fragment() {

    private var _binding: FragmentCalendarWeeklyBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var monthDayText: TextView
    private lateinit var hourListView : ListView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentCalendarWeeklyBinding.inflate(inflater, container, false)
        //setContentView(R.layout.activity_daily_calendar)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initWidgets()

        binding.weekButtonLeft.setOnClickListener {
            previousWeekAction()
            setWeekView()
        }

        binding.weekButtonRight.setOnClickListener {
            nextWeekAction()
            setWeekView()
        }
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

    fun previousWeekAction() {
        selectedDate = selectedDate!!.minusWeeks(1)
        setWeekView()
    }

    fun nextWeekAction() {
        selectedDate = selectedDate!!.plusWeeks(1)
        setWeekView()
    }

    fun newEventAction(view: View?) {
        //startActivity(Intent(this, EventEditActivity::class.java))
    }
}