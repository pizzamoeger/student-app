package com.hannah.studentapp.ui.event

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.hannah.studentapp.R
import com.hannah.studentapp.databinding.FragmentEventEditBinding
import com.hannah.studentapp.ui.calendar.CalendarUtils
import com.hannah.studentapp.ui.classesItem.ClassesItem
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.hannah.studentapp.ui.classesItem.EditClassFragmentDirections
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

class EventEditFragment : Fragment() {
    private var _binding: FragmentEventEditBinding? = null
    private val binding get() = _binding!!
    private val args: EventEditFragmentArgs by navArgs()

    var time: LocalTime = CalendarUtils.selectedTime
    var date: LocalDate = CalendarUtils.selectedDate
    var classItem: ClassesItem = ClassesItem()
    var event: Event? = null
    var fromTimeTable = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventEditBinding.inflate(inflater, container, false)
        val root: View = binding.root
        event = Event.get(args.eventId.toInt())
        fromTimeTable = args.fromTimeTable
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
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

    fun navBack() {
        if (fromTimeTable) {
            val navController = findNavController()
            val action = EventEditFragmentDirections.actionEditEventToWeekly()
            navController.navigate(action)
        } else {
            val navController = findNavController()
            val action = EventEditFragmentDirections.actionEditEventToDaily(fromTimetable = false, date=CalendarUtils.selectedDate.toString())
            navController.navigate(action)
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        //enable menu
        setHasOptionsMenu(true)

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(this){
                //true means that the callback is enabled
                this.isEnabled = true
                navBack()
                //exitDialog() //dialog to conform exit
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (event == null) {
            event = Event(date = date, time=time, classesItemId = 0, repeated = false)
        }

        // set repeated
        binding.checkBox.isChecked = event!!.isRepeated()

        // get event name
        binding.eventNameEditText.setText(event!!.getName())

        // assign text for date and time
        binding.pickDate.text = CalendarUtils.formattedDate(date)
        binding.pickTime.text = CalendarUtils.formattedShortTime(time)

        binding.editTextRoom.setText(event!!.getRoom())

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
            if (option.getId() == event!!.getClassId()) break
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
        event!!.setName(binding.eventNameEditText.text.toString(), requireContext())
        if (event!!.getName() == "") event!!.setName(classItem.toString(), requireContext()) // if event has no name we use class name as default

        // get repeated
        event!!.setRepeated(binding.checkBox.isChecked, requireContext())

        Event.delete(event!!.getId(), requireContext())

        event!!.setDate(date, requireContext())
        event!!.setTime(time, requireContext())
        event!!.setClassId(classItem.getId(), requireContext())
        event!!.setRoom(binding.editTextRoom.text.toString())
        Event.addEvent(event!!, requireContext())

        // hide keyboard again before heading up
        // so that layout is calculated correctly
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = requireActivity().currentFocus
        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }

        // navigate back
        navBack()
    }

    private fun deleteEvent() {
        // create a new event and add it to eventsList
        Event.delete(event!!.getId(), requireContext())
        navBack()
    }
}