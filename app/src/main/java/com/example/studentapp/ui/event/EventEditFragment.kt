package com.example.studentapp.ui.event

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Spinner
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.studentapp.R
import com.example.studentapp.SharedData
import com.example.studentapp.databinding.FragmentEventEditBinding
import com.example.studentapp.ui.calendar.CalendarUtils
import com.example.studentapp.ui.classesItem.ClassesItem
import java.time.LocalDate
import java.time.LocalTime

class EventEditFragment : Fragment() {
    private var _binding: FragmentEventEditBinding? = null
    private val binding get() = _binding!!

    var time: LocalTime = CalendarUtils.selectedTime
    var date: LocalDate = CalendarUtils.selectedDate
    var classItem: ClassesItem = SharedData.defaultClass

    // time picker
    private val timePickerDialogListener: TimePickerDialog.OnTimeSetListener =
        object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                // set attribute to selected time
                time = LocalTime.of(hourOfDay, minute)

                // set text to formatted time
                val formattedTime: String = CalendarUtils.formattedShortTime(time)
                binding.pickTime.text = formattedTime
            }
        }

    // date picker
    private val datePickerDialogListener: DatePickerDialog.OnDateSetListener =
        object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                // set attribute to selected date
                date = LocalDate.of(year, month+1, dayOfMonth) // selected date is 0 indexed bruh
                // set text
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

    // create and show timepicker
    private fun pickTime() {
        val timePicker = TimePickerDialog(requireContext(), timePickerDialogListener, time.hour, time.minute, true)
        timePicker.show()
    }

    // create and show datepicker
    private fun pickDate() {
        val datePicker = DatePickerDialog(requireContext(), datePickerDialogListener, date.year, date.monthValue-1, date.dayOfMonth)
        datePicker.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get event name
        binding.eventNameEditText.setText(SharedData.defaultClass.name)

        // assign text for date and time
        binding.pickDate.text = CalendarUtils.formattedDate(date)
        binding.pickTime.text = CalendarUtils.formattedShortTime(time)

        // bind pickDate and pickTime
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

        // select the class the event corresponds to
        // TODO organize this better
        val spinner: Spinner = binding.mySpinner

        // we can use actual classes or default class (which has no name and transparent background)
        val options = listOf(SharedData.defaultClass)+SharedData.classList.value.orEmpty()

        // we create a dropdown when the spinner is clicked
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) // it uses this as layout
        spinner.adapter = adapter

        // when item is selected
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // we set the class for this event to the selected
                classItem = options[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun saveEvent() {
        // get name
        var eventName = binding.eventNameEditText.text.toString()
        if (eventName == "") eventName = classItem.name // if event has no name we use class name as default

        // create a new event and add it to eventsList
        val newEvent = Event(name=eventName, date, time, classItem.id, repeated = true)
        Event.addEvent(newEvent)

        // hide keyboard again before heading up
        // so that layout is calculated correctly
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = requireActivity().currentFocus
        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }

        SharedData.saveEvent()

        // navigate back
        findNavController().navigateUp()
    }
}