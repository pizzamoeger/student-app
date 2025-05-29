package com.hannah.studentapp.ui.assignments.assignment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.hannah.studentapp.databinding.FragmentAssignmentBinding


class AssignmentFragment : Fragment() {
    private val args: AssignmentFragmentArgs by navArgs()

    var tvProgressLabel: TextView? = null
    var assignmentId : Int = -1 // temp
    var fromAssignmentNav = true

    private var _binding: FragmentAssignmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAssignmentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        assignmentId = args.assigntmentId.toInt()
        fromAssignmentNav = args.fromAssignmentNav
        return root
    }

    override fun onViewCreated(view:View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val thisAssignment = Assignment.get(assignmentId)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = thisAssignment.toString()


        // set a change listener on the SeekBar
        val seekBar = binding.seekBar
        seekBar.progress = thisAssignment.getProgress()
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener)

        val progress = seekBar.progress
        tvProgressLabel = binding.textView
        tvProgressLabel!!.setText("Progress: $progress")
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
                if (fromAssignmentNav) {
                    val navController = findNavController()
                    val action = AssignmentFragmentDirections.actionNavigationAssignmentToNavigationAssignments()
                    navController.navigate(action)
                } else {
                    val navController = findNavController()
                    val action = AssignmentFragmentDirections.actionNavigationAssignmentToFragmentCalendarDay()
                    navController.navigate(action)
                }

                //exitDialog() //dialog to conform exit
            }
    }

    var seekBarChangeListener: OnSeekBarChangeListener = object : OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            // updated continuously as the user slides the thumb
            tvProgressLabel!!.text = "Progress: $progress"
            Assignment.get(assignmentId).setProgress(progress, requireContext())
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {
            // called when the user first touches the SeekBar
        }

        override fun onStopTrackingTouch(seekBar: SeekBar) {
            // called after the user finishes moving the SeekBar
        }
    }
}