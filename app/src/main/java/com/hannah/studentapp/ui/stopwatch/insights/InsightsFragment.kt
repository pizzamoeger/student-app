package com.hannah.studentapp.ui.stopwatch.insights

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.hannah.studentapp.R
import com.hannah.studentapp.TimeInterval
import com.hannah.studentapp.databinding.FragmentInsightsBinding
import com.hannah.studentapp.ui.calendar.CalendarUtils
import com.hannah.studentapp.ui.classesItem.ClassesItem
import com.hannah.studentapp.ui.getThemeColor
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.time.LocalDate

class InsightsFragment : Fragment() {

    private var _binding: FragmentInsightsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInsightsBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
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
        val entries = ClassesItem.getList().filter { classItem ->
            classItem.getSeconds(type, from, to) != 0 // skip entry if it has not been studied
        }.map { classItem ->
            PieEntry(classItem.getSeconds(type, from, to).toFloat(), classItem.toString()) // Use actual values instead of 1f if you have them
        }

        val dataSet = PieDataSet(entries, "Subjects")

        // get the colors for the data entries
        dataSet.colors = ClassesItem.getList().filter { classItem ->
            classItem.getSeconds(type, from, to) != 0 // skip class if in has not been studied
        }.map { classItem ->
            classItem.getColor()
        }

        dataSet.valueTextSize = 14f
        dataSet.valueTextColor = requireContext().getThemeColor(android.R.attr.textColorSecondary)

        if (entries.isEmpty()) return null
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
        when {type == TimeInterval.MONTH -> pieChart.centerText = CalendarUtils.monthYearFromDate(LocalDate.now())}
        when {type == TimeInterval.WEEK -> pieChart.centerText = (context?.getString(R.string.week)
            ?: "Week") +CalendarUtils.weekFromDate(LocalDate.now())}
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
        val toolbar = binding.includedToolbar

        // left selection links to stopwatch
        context?.let { toolbar.textLeft.setTextColor(it.getThemeColor(com.google.android.material.R.attr.colorOnSurface)) }
        context?.let { toolbar.lineLeft.setBackgroundColor(it.getThemeColor(com.google.android.material.R.attr.colorOnSurface)) }
        toolbar.selectionLeft.setOnClickListener {
            val navController = findNavController()
            val action = InsightsFragmentDirections.actionInsightsToStopwatch()
            navController.navigate(action)
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