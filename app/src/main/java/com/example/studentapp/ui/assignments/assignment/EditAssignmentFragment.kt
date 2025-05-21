package com.example.studentapp.ui.assignments.assignment

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
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.studentapp.R
import com.example.studentapp.SharedData
import com.example.studentapp.databinding.FragmentAssignmentEditBinding
import com.example.studentapp.ui.assignments.assignment.Assignment
import com.example.studentapp.ui.calendar.CalendarUtils
import com.example.studentapp.ui.classesItem.ClassesItem
import com.google.android.material.datepicker.MaterialDatePicker
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

class EditAssignmentFragment : Fragment() {
    private var _binding: FragmentAssignmentEditBinding? = null
    private val binding get() = _binding!!
    private val args: EditAssignmentFragmentArgs by navArgs()

    var dueDate: LocalDate = CalendarUtils.selectedDate
    var classItem: ClassesItem = ClassesItem()
    var assignment: Assignment? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAssignmentEditBinding.inflate(inflater, container, false)
        val root: View = binding.root
        assignment = Assignment.get(args.assignmentId.toInt())
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        return root
    }

    // create and show datepicker
    private fun pickDate() {
        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select due date")
            .setSelection(dueDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()) // pre-select current value
            .setTheme(R.style.CustomDatePickerTheme) // optional custom theme
            .build()

        picker.addOnPositiveButtonClickListener { selection ->
            val instant = Instant.ofEpochMilli(selection)
            val selectedDate = instant.atZone(ZoneId.systemDefault()).toLocalDate()
            dueDate = selectedDate

            val formattedDate = CalendarUtils.formattedDate(dueDate)
            binding.pickDate.text = formattedDate
        }

        picker.show(parentFragmentManager, picker.toString())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (assignment == null) {
            assignment = Assignment(LocalDate.now(), 0, "", 0) // TODO temp
        }

        // set repeated
        // binding.checkBox.isChecked = event!!.repeated

        // get event name
        binding.nameText.setText(assignment!!.getTitle())

        // assign text for date and time
        binding.pickDate.text = CalendarUtils.formattedDate(dueDate)

        // bind pickDate and pickTime
        binding.pickDate.setOnClickListener{
            pickDate()
        }

        // bind button for saving
        binding.saveButton.setOnClickListener { _ ->
            save()
        }

        // bind button for deleting
        binding.deleteButton.setOnClickListener { _ ->
            delete()
        }

        // select the class the event corresponds to
        classSpinner()
    }

    private fun classSpinner () {
        val spinner: Spinner = binding.classSpinner

        // we can use actual classes or default class (which has no name and transparent background)
        val options = listOf(ClassesItem(id=0))+ClassesItem.getList()

        // we create a dropdown when the spinner is clicked
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) // it uses this as layout
        spinner.adapter = adapter

        var index = 0
        for (option in options) {
            if (option == assignment!!.getClass()) break
            index++
        }
        if (index == options.size) {
            Log.e("Assignment edit", "Invalid class id (class not in classlist)")
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
                classItem = assignment!!.getClass()
            }
        }
    }

    private fun save() {
        // get name
        assignment!!.setTitle(binding.nameText.text.toString(), requireContext())
        if (assignment!!.getTitle() == "") assignment!!.setTitle(classItem.toString(), requireContext()) // if event has no name we use class name as default

        Assignment.delete(assignment!!.getId(), requireContext())

        assignment!!.setDueDate(dueDate, requireContext())
        assignment!!.setClass(classItem.getId(), requireContext())

        // hide keyboard again before heading up
        // so that layout is calculated correctly
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = requireActivity().currentFocus
        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }

        Assignment.add(assignment!!, requireContext())

        // navigate back
        /*val navController = findNavController()
        val action = EditAssignmentFragmentDirections.actionFragmentEditAssignmentToNavigationAssignments()

        val navOptions = androidx.navigation.NavOptions.Builder() // TODO this is a bug in event navigatin back!!!!!
            .setPopUpTo(R.id.fragment_edit_assignment, true)
            .build()
        navController.navigate(action, navOptions)*/
        findNavController().navigateUp()
    }

    private fun delete() {
        // create a new event and add it to eventsList
        Assignment.delete(assignment!!.getId(), requireContext())
        val navController = findNavController()
        val action = EditAssignmentFragmentDirections.actionFragmentEditAssignmentToNavigationAssignments()

        val navOptions = androidx.navigation.NavOptions.Builder()
            .setPopUpTo(R.id.navigation_assignments, true)
            .build()
        navController.navigate(action, navOptions)
    }
}