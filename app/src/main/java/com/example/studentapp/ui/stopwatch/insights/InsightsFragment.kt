package com.example.studentapp.ui.stopwatch.insights

import android.os.Bundle
import android.util.Log
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
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

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
        val insightsViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[InsightsViewModel::class.java]

        val pieChart = binding.dailyChart
        daily(view, pieChart)

        insightsViewModel.entries.observe(viewLifecycleOwner) {
            pieChart.data = getDailyData()
            pieChart.invalidate()
        }
    }

    fun getDailyData() : PieData {
        val entries = SharedData.classList.value?.map { classItem ->
            PieEntry(classItem.secondsToday().toFloat(), classItem.name) // Use actual values instead of 1f if you have them
        }

        val dataSet = PieDataSet(entries, "Subjects")
        dataSet.colors = SharedData.classList.value?.map { classItem ->
            classItem.color// Use actual values instead of 1f if you have them
        }
        dataSet.valueTextSize = 14f
        dataSet.valueTextColor = requireContext().getThemeColor(android.R.attr.textColorSecondary)

        return PieData(dataSet)
    }

    fun daily(view : View, pieChart: PieChart) {
        if (SharedData.currentClass.value != null) {
            pieChart.data = getDailyData()
            pieChart.description.isEnabled = false
            pieChart.legend.isEnabled = false
            pieChart.animateY(1000)
            pieChart.invalidate() // refresh

            pieChart.setUsePercentValues(true)
            pieChart.centerText = "Daily"
            val primaryColor = requireContext().getThemeColor(android.R.attr.textColorPrimary)
            pieChart.setCenterTextColor(primaryColor)

            val secondaryColor = requireContext().getThemeColor(android.R.attr.textColorSecondary)
            pieChart.setEntryLabelColor(secondaryColor)

            val backgroundColor = requireContext().getThemeColor(android.R.attr.windowBackground)
            pieChart.setHoleColor(backgroundColor)
            pieChart.setTransparentCircleColor(backgroundColor)
            pieChart.setCenterTextSize(25f)
            pieChart.setEntryLabelTextSize(12f)
        } else pieChart.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()

        // display top navigation
        val activityBinding = (requireActivity() as MainActivity).binding
        val toolbar = activityBinding.includedToolbar

        // left selection links to stopwatch
        context?.let { toolbar.textLeft.setTextColor(it.getThemeColor(com.google.android.material.R.attr.colorOnSurface)) }
        context?.let { toolbar.lineLeft.setBackgroundColor(it.getThemeColor(com.google.android.material.R.attr.colorOnSurface)) }
        toolbar.selectionLeft.setOnClickListener {
            val action = InsightsFragmentDirections.actionInsightsToStopwatch()
            findNavController().navigate(action)
        }

        // right selection links to nothing (is current)
        context?.let { toolbar.textRight.setTextColor(it.getThemeColor(androidx.appcompat.R.attr.colorPrimary)) }
        context?.let { toolbar.lineRight.setBackgroundColor(it.getThemeColor(androidx.appcompat.R.attr.colorPrimary)) }
        toolbar.selectionRight.setOnClickListener {}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}