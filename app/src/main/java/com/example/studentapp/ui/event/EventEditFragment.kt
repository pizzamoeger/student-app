package com.example.studentapp.ui.event

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.studentapp.SharedData
import com.example.studentapp.databinding.FragmentEventEditBinding
import com.example.studentapp.ui.calendar.CalendarUtils
import java.time.LocalDate
import java.time.LocalTime

class EventEditFragment : Fragment() {
    private var _binding: FragmentEventEditBinding? = null
    private val binding get() = _binding!!

    var time: LocalTime = LocalTime.now()
    var date: LocalDate = LocalDate.now()

    private val timePickerDialogListener: TimePickerDialog.OnTimeSetListener =
        object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                time = LocalTime.of(hourOfDay, minute)
                val formattedTime: String = CalendarUtils.formattedShortTime(time)
                binding.pickTime.text = formattedTime
            }
        }

    private val datePickerDialogListener: DatePickerDialog.OnDateSetListener =
        object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                date = LocalDate.of(year, month+1, dayOfMonth) // months is 0 indexed glaub
                val formattedDate: String = CalendarUtils.formattedDate(date)
                binding.pickDate.text = formattedDate
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventEditBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    private fun pickTime() {
        val timePicker = TimePickerDialog(requireContext(), timePickerDialogListener, time.hour, time.minute, true)
        timePicker.show()
    }

    private fun pickDate() {
        val datePicker = DatePickerDialog(requireContext(), datePickerDialogListener, date.year, date.monthValue-1, date.dayOfMonth)
        datePicker.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.eventNameEditText.setText(SharedData.currentClass.value!!.name)

        // assign text for date and time
        binding.pickDate.text = CalendarUtils.formattedDate(date)
        binding.pickTime.text = CalendarUtils.formattedShortTime(time)

        // make text clickable and bind them
        binding.pickDate.setOnClickListener{
            pickDate()
        }
        binding.pickTime.setOnClickListener{
            pickTime()
        }

        // bind button for saving
        binding.saveButtonEvent.setOnClickListener { _ ->
            saveEvent()
        }
    }

    private fun saveEvent() {
        // get name
        val eventName = binding.eventNameEditText.text.toString()

        // create a new event and add it to eventsList
        val newEvent = Event(name=eventName, date, time, SharedData.currentClass.value!!)
        Event.eventsList.add(newEvent)

        // hide keyboard again before heading up
        // so that layout is calculated correctly
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = requireActivity().currentFocus
        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }

        // navigate back
        findNavController().navigateUp()
    }
}