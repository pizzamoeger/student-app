package com.hannah.studentapp.ui.grades

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.hannah.studentapp.databinding.AddGradesFragmentBinding
import com.hannah.studentapp.ui.classesItem.ClassesItem

class AddGradeFragment : Fragment() {
    private var _binding: AddGradesFragmentBinding? = null
    private val binding get() = _binding!!
    private val args: AddGradeFragmentArgs by navArgs()

    var classItem: ClassesItem = ClassesItem()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddGradesFragmentBinding.inflate(inflater, container, false)
        classItem = ClassesItem.get(args.classId)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.weightInput.setText("1.0")
        binding.gradeInput.setText("1.0")

        binding.saveButton.setOnClickListener{
            save()
        }
    }

    fun save() {
        val weight = binding.weightInput.text.toString().toFloatOrNull()!!
        val grade = binding.gradeInput.text.toString().toFloatOrNull()!!
        classItem.addGrade(grade,weight, requireContext())
        findNavController().navigateUp()
    }
}