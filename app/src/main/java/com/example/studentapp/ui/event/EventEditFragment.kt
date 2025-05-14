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
import androidx.navigation.fragment.navArgs
import com.example.studentapp.R
import com.example.studentapp.SharedData
import com.example.studentapp.databinding.FragmentEventEditBinding
import com.example.studentapp.ui.calendar.CalendarUtils
import com.example.studentapp.ui.classesItem.ClassesItem
import com.example.studentapp.ui.classesItem.ClassesItemFragmentArgs
import java.time.LocalDate
import java.time.LocalTime
import kotlin.properties.Delegates

class EventEditFragment : Fragment() {
    private var _binding: FragmentEventEditBinding? = null
    private val binding get() = _binding!!
    private val args: EventEditFragmentArgs by navArgs()

    var time: LocalTime = CalendarUtils.selectedTime
    var date: LocalDate = CalendarUtils.selectedDate
    var classItem: ClassesItem = ClassesItem()
    var event: Event? = null

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
        event = Event.get(args.eventId.toInt())
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
        binding.saveButtonEvent.setOnClickListener { _ ->
            saveEvent()
        }

        // bind button for deleting
        binding.deleteButtonEvent.setOnClickListener { _ ->
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
            if (option.id == event!!.classesItemId) break
            index++
        }
        spinner.setSelection(index)

        // when item is selected
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
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
        if (event!!.name == "") event!!.name = classItem.name // if event has no name we use class name as default

        // get repeated
        event!!.repeated = binding.checkBox.isChecked

        Event.delete(event!!.id)

        event!!.date = date
        event!!.time = time
        event!!.classesItemId = classItem.id
        Event.addEvent(event!!)

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

    private fun deleteEvent() {
        // create a new event and add it to eventsList
        Event.delete(event!!.id)
        SharedData.saveEvent()
        findNavController().navigateUp()
    }
}