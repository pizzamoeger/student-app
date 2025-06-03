package com.hannah.studentapp.ui.classesItem

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.hannah.studentapp.R
import com.hannah.studentapp.databinding.FragmentEditClassBinding
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.hannah.studentapp.ui.type.Type
import kotlin.random.Random

class EditClassFragment : Fragment() {
    private val args: ClassesItemFragmentArgs by navArgs()
    private var _binding: FragmentEditClassBinding? = null
    private val binding get() = _binding!!
    private var _classId : Int = 0

    private var color : Int = 0
    private var curType : Int = 0
    private var oldType : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _classId = args.classId.toInt()
        _binding = FragmentEditClassBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
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

    override fun onAttach(context: Context) {
        super.onAttach(context)

        //enable menu
        setHasOptionsMenu(true)

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(this){
                //true means that the callback is enabled
                this.isEnabled = true
                val navController = findNavController()
                val action = EditClassFragmentDirections.actionEditClassToClasses()

                val navOptions = androidx.navigation.NavOptions.Builder()
                    .setPopUpTo(R.id.fragment_edit_class, false)
                    .build()
                navController.navigate(action, navOptions)
                //exitDialog() //dialog to conform exit
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get the class
        var thisClass = ClassesItem.get(_classId)
        if (_classId == -1) thisClass = ClassesItem(id=_classId, color = randomColor(), name = "Class ${System.currentTimeMillis() % 1000}")

        // set text of textinput to classname
        binding.eventNameEditText.setText(thisClass.toString())

        binding.classEctsInput.setText(thisClass.getECTS().toString())

        binding.checkboxPassed.isChecked = thisClass.isPassed()

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

        // get the type it belongs to
        val options = listOf(Type(id=0, name="None"))+Type.getList()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) // it uses this as layout
        binding.typeSpinner.adapter = adapter

        var index = 0
        for (option in options) {
            if (option.containsClass(thisClass.getId())) break
            index++
        }
        if (index == options.size) {
            Log.e("Class edit", "class does not belong to any type. adding it to 0")
            options[0].addClass(thisClass.getId())
            index = 0
        }
        binding.typeSpinner.setSelection(index)
        curType = options[index].getID()
        oldType = options[index].getID()

                // when item is selected
        binding.typeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // we set the class for this event to the selected
                curType = options[position].getID()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                //classItem = SharedData.get(event!!.classesItemId)
            }
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
        thisClass.setECTS(binding.classEctsInput.text.toString().toInt())
        thisClass.setName(binding.eventNameEditText.text.toString(), requireContext())
        thisClass.setColor(color, requireContext())
        thisClass.setPassed(binding.checkboxPassed.isChecked)

        if (oldType != 0) {
            Type.get(oldType).removeClass(thisClass.getId())
        }
        if (curType != 0) {
            Type.get(curType).addClass(thisClass.getId())
        }

        ClassesItem.add(thisClass, requireContext())

        // go back
        val navController = findNavController()
        val action = EditClassFragmentDirections.actionEditClassToClasses()

        val navOptions = androidx.navigation.NavOptions.Builder()
            .setPopUpTo(R.id.navigation_classes, false)
            .build()
        navController.navigate(action, navOptions)
    }
}