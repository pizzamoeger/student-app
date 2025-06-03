package com.hannah.studentapp.ui.type

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hannah.studentapp.databinding.EctsFragmentBinding
import com.hannah.studentapp.databinding.FragmentSemesterBinding
import com.hannah.studentapp.ui.semester.Semester

class TypeFragment : Fragment() {
    private var _binding : EctsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EctsFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }
}