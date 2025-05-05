package com.example.studentapp.ui.classesItem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.studentapp.databinding.FragmentClassesItemBinding
import com.example.studentapp.databinding.FragmentTimetableBinding
import com.example.studentapp.ui.stopwatch.StopwatchViewModel

class ClassesItemFragment : Fragment() {
    private val args: ClassesItemFragmentArgs by navArgs()
    private var _binding: FragmentClassesItemBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClassesItemBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val classesItemViewModel =
            ViewModelProvider(this).get(ClassesItemViewModel::class.java)
        classesItemViewModel.setId(args.classId.toInt())

        val textView: TextView = binding.textClassesItem
        classesItemViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        // Use classId to fetch and display class data
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}