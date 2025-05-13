package com.example.studentapp.ui.classesItem

import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.studentapp.R
import com.example.studentapp.SharedData
import com.example.studentapp.databinding.FragmentEditClassBinding
import com.example.studentapp.ui.calendar.CalendarUtils
import com.example.studentapp.ui.event.Event
import com.example.studentapp.ui.getThemeColor
import java.time.LocalDate
import java.time.LocalTime
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.ColorPickerDialog

class EditClassFragment : Fragment() {
    private val args: ClassesItemFragmentArgs by navArgs()
    private var _binding: FragmentEditClassBinding? = null
    private val binding get() = _binding!!
    private var _classId : Int = 0

    private var color : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _classId = args.classId.toInt()
        _binding = FragmentEditClassBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    // color picker
    private fun pickColor(view: View, color: Int) {
        ColorPickerDialog.Builder(requireContext())
            .setDefaultColor(color) // color at the start
            .setColorListener({ col, _ ->
                binding.classColorInput.setBackgroundColor(col) // set background
                this.color = col // set the attribute
            })
            .build()
            .show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get the class
        val thisClass = ClassesItem.get(_classId)

        // set text of textinput to classname
        binding.eventNameEditText.setText(thisClass.name)

        // set color to color of thisClass
        binding.classColorInput.setBackgroundColor(thisClass.color)
        color = thisClass.color

        // make text clickable and bind
        binding.classColorInput.apply {
            isFocusable = false
            isClickable = true
        }
        binding.classColorInput.setOnClickListener{ v ->
            pickColor(v, thisClass.color)
        }

        // bind button for saving
        binding.saveButtonClass.setOnClickListener { _ ->
            saveClass()
        }
    }

    private fun saveClass() {
        val thisClass = ClassesItem.get(_classId)

        // get name
        thisClass.name = binding.eventNameEditText.text.toString()
        thisClass.color = color

        ClassesItem.save()

        // go back
        findNavController().navigateUp()
    }
}