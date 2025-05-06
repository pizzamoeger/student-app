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
        // hide default toolbar and display top level navigation
        (requireActivity() as MainActivity).hideDefaultToolbar()

        val activityBinding = (requireActivity() as MainActivity).binding
        val toolbar = activityBinding.includedToolbar

        // text right links to stopwatch
        toolbar.textLeft.setTextColor(resources.getColor(android.R.color.darker_gray, requireContext().theme))
        toolbar.textLeft.setOnClickListener {
            val action = InsightsFragmentDirections.actionInsightsToStopwatch()
            findNavController().navigate(action)
        }

        // text right links to nothing (is current)
        toolbar.textRight.setTextColor(resources.getColor(R.color.black, requireContext().theme))
        toolbar.textRight.setOnClickListener {}
    }

    override fun onPause() {
        super.onPause()
        // show default toolbar again
        (requireActivity() as MainActivity).showDefaultToolbar()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}