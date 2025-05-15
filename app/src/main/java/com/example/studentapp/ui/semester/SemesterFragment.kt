package com.example.studentapp.ui.semester

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
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.studentapp.R
import com.example.studentapp.databinding.FragmentEventEditBinding
import com.example.studentapp.databinding.FragmentSemesterBinding
import com.example.studentapp.ui.calendar.CalendarUtils
import com.example.studentapp.ui.classesItem.ClassesItem
import com.example.studentapp.ui.event.Event
import com.example.studentapp.ui.event.EventEditFragmentArgs
import com.example.studentapp.ui.event.EventEditFragmentDirections
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

class SemesterFragment : Fragment() {
    private var _binding: FragmentSemesterBinding? = null
    private val binding get() = _binding!!

    var from: LocalDate = CalendarUtils.selectedDate
    var to: LocalDate = CalendarUtils.selectedDate.plusMonths(6)
    lateinit var semester: Semester

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSemesterBinding.inflate(inflater, container, false)
        val root: View = binding.root
        if (Semester.getList().isEmpty()) Semester.add()
        semester = Semester.getList()[0]
        return root
    }

    // create and show datepicker
    private fun pickDate(textView : TextView, setDate : (LocalDate) -> Unit, date: LocalDate) {
        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .setSelection(date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()) // pre-select current value
            .setTheme(R.style.CustomDatePickerTheme) // optional custom theme
            .build()

        picker.addOnPositiveButtonClickListener { selection ->
            val instant = Instant.ofEpochMilli(selection)
            val selectedDate = instant.atZone(ZoneId.systemDefault()).toLocalDate()
            setDate(selectedDate)

            val formattedDate = CalendarUtils.formattedDate(selectedDate)
            textView.text = formattedDate
        }

        picker.show(parentFragmentManager, picker.toString())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        from = semester.getStart()
        to = semester.getEnd()

        // get event name
        binding.nameEditText.setText(semester.toString())

        // assign text for date and time
        binding.dateFrom.text = CalendarUtils.formattedDate(from)
        binding.dateTo.text = CalendarUtils.formattedDate(to)

        // bind pickDate and pickTime
        binding.dateFrom.setOnClickListener{
            pickDate(binding.dateFrom, {d -> from=d}, from)
        }
        binding.dateTo.setOnClickListener{
            pickDate(binding.dateTo, {d -> to=d}, to)
        }

        // bind button for saving
        binding.saveButton.setOnClickListener { _ ->
            saveSemester()
        }

        // bind button for deleting
        binding.deleteButton.setOnClickListener { _ ->
            deleteSemester()
        }

        // select the class the event corresponds to
        // TODO organize this better
        val spinner: Spinner = binding.semesterSpinner

        // we can use actual classes or default class (which has no name and transparent background)
        var options = Semester.getList()
        if (options.isEmpty()) Semester.add()
        options = Semester.getList()

        // we create a dropdown when the spinner is clicked
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) // it uses this as layout
        spinner.adapter = adapter

        spinner.setSelection(0)

        // when item is selected
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // we set the class for this event to the selected
                semester = options[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                //classItem = SharedData.get(event!!.classesItemId)
            }
        }
    }

    private fun saveSemester() {
        // get name
        semester.setName(binding.nameEditText.text.toString())
        if (semester.toString() == "")  semester.setName(id.toString()) // if event has no name we use class name as default

        Semester.delete(semester.getId())

        Semester.add(from, to, binding.nameEditText.text.toString())

        // hide keyboard again before heading up
        // so that layout is calculated correctly
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = requireActivity().currentFocus
        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }

        // navigate back
        val navController = findNavController()
        navController.navigateUp()
    }

    private fun deleteSemester() {
        // create a new event and add it to eventsList
        /*Event.delete(event!!.id)
        val navController = findNavController()
        val action = EventEditFragmentDirections.actionEditEventToWeekly()

        val navOptions = androidx.navigation.NavOptions.Builder()
            .setPopUpTo(R.id.navigation_classes, true)
            .build()
        navController.navigate(action, navOptions)*/
    }
}