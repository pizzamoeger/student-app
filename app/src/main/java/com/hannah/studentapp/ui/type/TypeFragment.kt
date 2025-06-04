package com.hannah.studentapp.ui.type

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hannah.studentapp.databinding.EctsFragmentBinding
import com.hannah.studentapp.ui.classesItem.ClassesItem
import com.hannah.studentapp.ui.grades.GradesForClassFragmentDirections

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addButton.setOnClickListener{
            val navController = findNavController()
            val action = TypeFragmentDirections.actionHurensohnToHurensohn2(-1)
            navController.navigate(action)
        }

        val adapter = TypeAdapter()
        binding.typeListView.layoutManager = LinearLayoutManager(requireContext())
        binding.typeListView.adapter = adapter
    }
}