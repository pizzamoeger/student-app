package com.example.studentapp.ui.timetable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.studentapp.databinding.FramentEventEditBinding
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
        requireActivity().supportFragmentManager.popBackStack()
    }
}