package com.example.studentapp.ui.stopwatch.insights

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentapp.MainActivity
import com.example.studentapp.R
import com.example.studentapp.SharedData
import com.example.studentapp.databinding.FragmentInsightsBinding
import com.example.studentapp.ui.classes.StopwatchAdapter
import com.example.studentapp.ui.getThemeColor
import com.example.studentapp.ui.stopwatch.insights.InsightsFragmentDirections
import com.example.studentapp.ui.stopwatch.StopwatchViewModel

class InsightsFragment : Fragment() {

    private var _binding: FragmentInsightsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInsightsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // TODO
    }

    override fun onResume() {
        super.onResume()

        // display top navigation
        val activityBinding = (requireActivity() as MainActivity).binding
        val toolbar = activityBinding.includedToolbar

        // left selection links to stopwatch
        context?.let { toolbar.textLeft.setTextColor(it.getThemeColor(android.R.attr.textColorTertiary)) }
        context?.let { toolbar.lineLeft.setBackgroundColor(it.getThemeColor(android.R.attr.textColorTertiary)) }
        toolbar.selectionLeft.setOnClickListener {
            val action = InsightsFragmentDirections.actionInsightsToStopwatch()
            findNavController().navigate(action)
        }

        // right selection links to nothing (is current)
        context?.let { toolbar.textRight.setTextColor(it.getThemeColor(android.R.attr.textColorSecondary)) }
        context?.let { toolbar.lineRight.setBackgroundColor(it.getThemeColor(android.R.attr.textColorSecondary)) }
        toolbar.selectionRight.setOnClickListener {}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}