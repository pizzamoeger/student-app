package com.example.studentapp.ui.semester

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.studentapp.databinding.FragmentSemesterBinding

class SemesterFragment : Fragment() {

    private var _binding: FragmentSemesterBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSemesterBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}