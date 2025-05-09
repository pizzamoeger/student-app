package com.example.studentapp.ui.event

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.studentapp.databinding.FragmentEventEditBinding
import com.example.studentapp.ui.calendar.CalendarUtils
import java.time.LocalTime

class EventEditFragment : Fragment() {
    private var _binding: FragmentEventEditBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventEditBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO make user be able to select this
        // assign text for date and time
        val dateStr = "Date: ${CalendarUtils.formattedDate(CalendarUtils.selectedDate)}"
        val timeStr = "Time: ${CalendarUtils.formattedTime(LocalTime.now())}"
        binding.eventDateTextView.text = dateStr
        binding.eventTimeTextView.text = timeStr

        // bind button for saving
        binding.saveButtonEvent.setOnClickListener { _ ->
            saveEvent()
        }
    }

    private fun saveEvent() {
        // get name
        val eventName = binding.eventNameEditText.text.toString()

        // create a new event and add it to eventsList
        val newEvent = Event(name=eventName, CalendarUtils.selectedDate, LocalTime.now())
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