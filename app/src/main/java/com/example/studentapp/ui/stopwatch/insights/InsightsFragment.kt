package com.example.studentapp.ui.stopwatch.insights

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.studentapp.MainActivity
import com.example.studentapp.R
import com.example.studentapp.SharedData
import com.example.studentapp.TimeInterval
import com.example.studentapp.databinding.FragmentInsightsBinding
import com.example.studentapp.ui.getThemeColor
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

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

        // daily chart
        bindPieChart(
            item=insightsViewModel.entriesDay,
            type = TimeInterval.DAY,
            pieChart = binding.dailyChart)

        // weekly chart
        bindPieChart(
            item=insightsViewModel.entriesWeek,
            type = TimeInterval.WEEK,
            pieChart = binding.weeklyChart)

        // monthly chart
        bindPieChart(
            item=insightsViewModel.entriesMonth,
            type = TimeInterval.MONTH,
            pieChart = binding.monthlyChart)
    }

    // bind pie chart item to its stuff
    private fun bindPieChart(item : LiveData<List<PieEntry>>, type: TimeInterval, pieChart: PieChart) {
        createPieChart(view=requireView(), pieChart=pieChart, type=type)
        item.observe(viewLifecycleOwner) {
            pieChart.data = getDataPieChart(type)
            if (pieChart.data != null) pieChart.invalidate()
        }
    }

    // returns the data for the pie chart or null if there are no entries
    private fun getDataPieChart(type : TimeInterval = TimeInterval.TOTAL, from : String = "", to : String = "") : PieData? {

        // get the entries
        val entries = SharedData.classList.value?.filter { classItem ->
            classItem.getSeconds(type, from, to) != 0 // skip entry if it has not been studied
        }?.map { classItem ->
            PieEntry(classItem.getSeconds(type, from, to).toFloat(), classItem.name) // Use actual values instead of 1f if you have them
        }

        val dataSet = PieDataSet(entries, "Subjects")

        // get the colors for the data entries
        dataSet.colors = SharedData.classList.value?.filter { classItem ->
            classItem.getSeconds(type, from, to) != 0 // skip class if in has not been studied
        }?.map { classItem ->
            classItem.color
        }

        dataSet.valueTextSize = 14f
        dataSet.valueTextColor = requireContext().getThemeColor(android.R.attr.textColorSecondary)

        if (entries != null) {
            if (entries.isEmpty()) return null
        }
        return PieData(dataSet)
    }

    // creates PieChart from data
    private fun createPieChart(view : View, pieChart: PieChart, type: TimeInterval) {
        pieChart.description.isEnabled = false
        pieChart.legend.isEnabled = false
        pieChart.animateY(1000)
        pieChart.invalidate()

        // center text
        when {type == TimeInterval.TOTAL -> pieChart.centerText = "Total" }
        when {type == TimeInterval.MONTH -> pieChart.centerText = "This Month"} // todo month name
        when {type == TimeInterval.WEEK -> pieChart.centerText = "This Week"} // todo KW
        when {type == TimeInterval.DAY -> pieChart.centerText = "Today"}
        when {type == TimeInterval.DEFAULT -> pieChart.centerText = "Custom Interval"}

        val primaryColor = requireContext().getThemeColor(android.R.attr.textColorPrimary)
        pieChart.setCenterTextColor(primaryColor)
        pieChart.setCenterTextSize(25f)

        // entry labels
        val secondaryColor = requireContext().getThemeColor(android.R.attr.textColorSecondary)
        pieChart.setEntryLabelColor(secondaryColor)
        pieChart.setEntryLabelTextSize(12f)

        // hole
        val backgroundColor = requireContext().getThemeColor(android.R.attr.windowBackground)
        pieChart.setHoleColor(backgroundColor)
        pieChart.setTransparentCircleColor(backgroundColor)
    }

    override fun onResume() {
        super.onResume()

        // display top navigation
        val activityBinding = (requireActivity() as MainActivity).binding
        val toolbar = binding.includedToolbar

        // left selection links to stopwatch
        context?.let { toolbar.textLeft.setTextColor(it.getThemeColor(com.google.android.material.R.attr.colorOnSurface)) }
        context?.let { toolbar.lineLeft.setBackgroundColor(it.getThemeColor(com.google.android.material.R.attr.colorOnSurface)) }
        toolbar.selectionLeft.setOnClickListener {
            val action = InsightsFragmentDirections.actionInsightsToStopwatch()
            findNavController().popBackStack()
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