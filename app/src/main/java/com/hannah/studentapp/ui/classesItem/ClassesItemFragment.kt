package com.hannah.studentapp.ui.classesItem

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.hannah.studentapp.databinding.FragmentClassesItemBinding
import com.hannah.studentapp.ui.getThemeColor

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

        // create viewmodel and setId
        val classesItemViewModel =
            ViewModelProvider(this).get(ClassesItemViewModel::class.java)
        classesItemViewModel.setId(_classId)

        // set the label of this fragment
        val thisClass = ClassesItem.get(_classId)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = thisClass.toString()

        // bind all time informations
        bindTimeInformation(binding.timeDailyClassesItem, classesItemViewModel.dailyTime)
        bindTimeInformation(binding.timeWeeklyClassesItem, classesItemViewModel.weeklyTime)
        bindTimeInformation(binding.timeMonthlyClassesItem, classesItemViewModel.monthlyTime)
        bindTimeInformation(binding.timeTotalClassesItem, classesItemViewModel.totalTime)

        // sets backgroundcolor of toolbar to color of this class
        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.setBackgroundDrawable(
            ColorDrawable(thisClass.getColor())
        )
    }

    // binding textView to stringTime
    fun bindTimeInformation(textView : TextView, stringTime : LiveData<String>) {
        stringTime.observe(viewLifecycleOwner) {
            textView.text = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        val activity = requireActivity() as AppCompatActivity
        // resets color of toolbar
        activity.supportActionBar?.setBackgroundDrawable(
            ColorDrawable(requireContext().getThemeColor(androidx.appcompat.R.attr.colorPrimary))
        )
    }
}