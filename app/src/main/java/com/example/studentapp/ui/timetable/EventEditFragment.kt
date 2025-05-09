package com.example.studentapp.ui.timetable

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.studentapp.R
import com.example.studentapp.databinding.FramentEventEditBinding
import com.example.studentapp.ui.calendar.CalendarUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalTime

class EventEditFragment : Fragment() {
    private var _binding: FramentEventEditBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var eventNameET : EditText
    lateinit var eventDateTV : TextView
    lateinit var eventTimeTV : TextView
    lateinit var time : LocalTime

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FramentEventEditBinding.inflate(inflater, container, false)
        val root: View = binding.root

        eventNameET = binding.eventNameEditText
        eventDateTV = binding.eventDateTextView
        eventTimeTV = binding.eventTimeTextView

        time = LocalTime.now()
        eventDateTV.text = "Date: ${CalendarUtils.formattedDate(CalendarUtils.selectedDate!!)}"
        eventTimeTV.text = "Date: ${CalendarUtils.formattedTime(time)}"

        binding.saveButtonEvent.setOnClickListener { _ ->
            saveEvent()
        }

        return root
    }

    fun saveEvent() {
        val eventName = eventNameET.text.toString()
        val newEvent = Event(name=eventName, CalendarUtils.selectedDate!!, time)
        Event.eventsList.add(newEvent)

        // Hide the keyboard before navigating up
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = requireActivity().currentFocus
        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }

        // Now navigate back
        findNavController().navigateUp()
    }
}