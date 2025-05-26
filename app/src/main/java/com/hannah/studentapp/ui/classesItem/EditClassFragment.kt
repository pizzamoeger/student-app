package com.hannah.studentapp.ui.classesItem

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.hannah.studentapp.R
import com.hannah.studentapp.databinding.FragmentEditClassBinding
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import kotlin.random.Random

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
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        val root: View = binding.root
        return root
    }

    // color picker
    private fun pickColor(view: View, color: Int) {
        ColorPickerDialog.Builder(requireContext())
            .setDefaultColor(color) // color at the start
            .setColorListener{ col, _ ->
                binding.classColorInput.setBackgroundColor(col) // set background
                this.color = col // set the attribute
            }
            .build()
            .show()
    }

    private fun randomColor(): Int {
        val random = Random.Default
        val hsv = floatArrayOf(random.nextFloat()*360, 0.8F, 0.9F)
        return Color.HSVToColor(hsv)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get the class
        var thisClass = ClassesItem.get(_classId)
        if (_classId == -1) thisClass = ClassesItem(id=_classId, color = randomColor(), name = "Class ${System.currentTimeMillis() % 1000}")

        // set text of textinput to classname
        binding.eventNameEditText.setText(thisClass.toString())

        // set color to color of thisClass
        color = thisClass.getColor()
        binding.classColorInput.setBackgroundColor(color)


        // make text clickable and bind
        binding.classColorInput.apply {
            isFocusable = false
            isClickable = true
        }
        binding.classColorInput.setOnClickListener{ v ->
            pickColor(v, thisClass.getColor())
        }

        // bind button for saving
        binding.saveButton.setOnClickListener { _ ->
            saveClass()
        }


        // bind delete button
        binding.deleteButton.setOnClickListener {
            deleteClass()
        }
    }

    private fun deleteClass() {
        ClassesItem.delete(_classId, requireContext())

        val navController = findNavController()
        val action = EditClassFragmentDirections.actionEditClassToClasses()

        val navOptions = androidx.navigation.NavOptions.Builder()
            .setPopUpTo(R.id.navigation_classes, false)
            .build()
        navController.navigate(action, navOptions)
    }

    private fun saveClass() {
        val thisClass = ClassesItem.get(_classId)
        ClassesItem.delete(_classId, requireContext())

        // get name
        thisClass.setName(binding.eventNameEditText.text.toString(), requireContext())
        thisClass.setColor(color, requireContext())

        ClassesItem.add(thisClass.toString(), thisClass.getColor(), requireContext())

        // go back
        val navController = findNavController()
        val action = EditClassFragmentDirections.actionEditClassToClasses()

        val navOptions = androidx.navigation.NavOptions.Builder()
            .setPopUpTo(R.id.navigation_classes, false)
            .build()
        navController.navigate(action, navOptions)
    }
}