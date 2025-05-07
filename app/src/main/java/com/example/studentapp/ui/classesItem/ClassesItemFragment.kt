package com.example.studentapp.ui.classesItem

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.studentapp.R
import com.example.studentapp.SharedData
import com.example.studentapp.databinding.FragmentClassesItemBinding
import com.example.studentapp.databinding.FragmentTimetableBinding
import com.example.studentapp.ui.getThemeColor
import com.example.studentapp.ui.stopwatch.StopwatchViewModel

class ClassesItemFragment : Fragment() {
    private val args: ClassesItemFragmentArgs by navArgs()
    private var _binding: FragmentClassesItemBinding? = null
    private var _classId : Int = 0

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // sets classId
        _classId = args.classId.toInt()

        _binding = FragmentClassesItemBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // update Date as we are displaying time again
        SharedData.updateDate()

        // create viewmodel and setId
        val classesItemViewModel =
            ViewModelProvider(this).get(ClassesItemViewModel::class.java)
        classesItemViewModel.setId(_classId)

        // set the label of this fragment
        val thisClass = SharedData.classList.value?.find { it.id == _classId }
        (requireActivity() as AppCompatActivity).supportActionBar?.title = thisClass!!.name

        // bind all time informations
        val dailyTime: TextView = binding.timeDailyClassesItem
        classesItemViewModel.dailyTime.observe(viewLifecycleOwner) {
            dailyTime.text = it
        }

        val monthlyTime: TextView = binding.timeMonthlyClassesItem
        classesItemViewModel.monthlyTime.observe(viewLifecycleOwner) {
            monthlyTime.text = it
        }

        val weeklyTime: TextView = binding.timeWeeklyClassesItem
        classesItemViewModel.weeklyTime.observe(viewLifecycleOwner) {
            weeklyTime.text = it
        }

        val totalTime: TextView = binding.timeTotalClassesItem
        classesItemViewModel.totalTime.observe(viewLifecycleOwner) {
            totalTime.text = it
        }

        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.setBackgroundDrawable(
            ColorDrawable(thisClass.color)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.setBackgroundDrawable(
            ColorDrawable(requireContext().getThemeColor(androidx.appcompat.R.attr.colorPrimary))
        )
    }
}