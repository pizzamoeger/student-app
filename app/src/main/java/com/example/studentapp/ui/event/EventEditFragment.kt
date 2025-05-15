package com.example.studentapp.ui.event

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
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
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.studentapp.R
import com.example.studentapp.SharedData
import com.example.studentapp.databinding.FragmentEventEditBinding
import com.example.studentapp.ui.calendar.CalendarUtils
import com.example.studentapp.ui.classesItem.ClassesItem
import com.example.studentapp.ui.classesItem.ClassesItemFragmentArgs
import com.example.studentapp.ui.classesItem.EditClassFragmentDirections
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import kotlin.properties.Delegates

class EventEditFragment : Fragment() {
    private var _binding: FragmentEventEditBinding? = null
    private val binding get() = _binding!!
    private val args: EventEditFragmentArgs by navArgs()

    var time: LocalTime = CalendarUtils.selectedTime
    var date: LocalDate = CalendarUtils.selectedDate
    var classItem: ClassesItem = ClassesItem()
    var event: Event? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventEditBinding.inflate(inflater, container, false)
        val root: View = binding.root
        event = Event.get(args.eventId.toInt())
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        return root
    }

    // create and show timepicker
    private fun pickTime() {
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H) // or TimeFormat.CLOCK_12H
            .setHour(12)
            .setMinute(0)
            .setTheme(R.style.CustomTimePicker)
            .build()

        picker.show(parentFragmentManager, "TIME_PICKER")

        picker.addOnPositiveButtonClickListener {
            val hour = picker.hour
            val minute = picker.minute
            // set attribute to selected time
            time = LocalTime.of(hour, minute)

            // set text to formatted time
            val formattedTime: String = CalendarUtils.formattedShortTime(time)
            binding.pickTime.text = formattedTime
        }
    }

    // create and show datepicker
    private fun pickDate() {
        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .setSelection(date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()) // pre-select current value
            .setTheme(R.style.CustomDatePickerTheme) // optional custom theme
            .build()

        picker.addOnPositiveButtonClickListener { selection ->
            val instant = Instant.ofEpochMilli(selection)
            val selectedDate = instant.atZone(ZoneId.systemDefault()).toLocalDate()
            date = selectedDate

            val formattedDate = CalendarUtils.formattedDate(date)
            binding.pickDate.text = formattedDate
        }

        picker.show(parentFragmentManager, picker.toString())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (event == null) {
            event = Event(name="", date = date, time=time, classesItemId = 0, repeated = false)
        }

        // set repeated
        binding.checkBox.isChecked = event!!.repeated

        // get event name
        binding.eventNameEditText.setText(event!!.name)

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
        binding.saveButton.setOnClickListener { _ ->
            saveEvent()
        }

        // bind button for deleting
        binding.deleteButton.setOnClickListener { _ ->
            deleteEvent()
        }

        // select the class the event corresponds to
        // TODO organize this better
        val spinner: Spinner = binding.mySpinner

        // we can use actual classes or default class (which has no name and transparent background)
        val options = listOf(ClassesItem(id=0))+ClassesItem.getList()

        // we create a dropdown when the spinner is clicked
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) // it uses this as layout
        spinner.adapter = adapter

        var index = 0
        for (option in options) {
            if (option.getId() == event!!.classesItemId) break
            index++
        }
        if (index == options.size) {
            Log.e("Event edit", "Invalid class id (class not in classlist)")
            index = 0
        }
        spinner.setSelection(index)

        // when item is selected
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // we set the class for this event to the selected
                classItem = options[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                //classItem = SharedData.get(event!!.classesItemId)
            }
        }
    }

    private fun saveEvent() {
        // get name
        event!!.name = binding.eventNameEditText.text.toString()
        if (event!!.name == "") event!!.name = classItem.toString() // if event has no name we use class name as default

        // get repeated
        event!!.repeated = binding.checkBox.isChecked

        Event.delete(event!!.id)

        event!!.date = date
        event!!.time = time
        event!!.classesItemId = classItem.getId()
        Event.addEvent(event!!)

        // hide keyboard again before heading up
        // so that layout is calculated correctly
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = requireActivity().currentFocus
        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }

        // navigate back
        val navController = findNavController()
        val action = EventEditFragmentDirections.actionEditEventToWeekly()

        val navOptions = androidx.navigation.NavOptions.Builder()
            .setPopUpTo(R.id.navigation_classes, true)
            .build()
        navController.navigate(action, navOptions)
    }

    private fun deleteEvent() {
        // create a new event and add it to eventsList
        Event.delete(event!!.id)
        val navController = findNavController()
        val action = EventEditFragmentDirections.actionEditEventToWeekly()

        val navOptions = androidx.navigation.NavOptions.Builder()
            .setPopUpTo(R.id.navigation_classes, true)
            .build()
        navController.navigate(action, navOptions)
    }
}