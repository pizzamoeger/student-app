package com.hannah.studentapp.ui.type

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.hannah.studentapp.databinding.EditTypeFragmentBinding

class EditTypeFragment : Fragment() {
    private var _binding: EditTypeFragmentBinding? = null
    private val binding get() = _binding!!
    private val args : EditTypeFragmentArgs by navArgs()
    private var type: Type? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EditTypeFragmentBinding.inflate(inflater, container, false)
        if (args.typeId == -1) type = Type(neededEcts = 5, name = "Type")
        else type = Type.get(args.typeId)
        val root : View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.typeNameEditText.setText(type!!.getName())
        binding.neededEctsInput.setText(type!!.getECTSNeeded().toString())

        binding.deleteButton.setOnClickListener{
            delete()
        }
        binding.saveButton.setOnClickListener{
            save()
        }
    }

    fun redirectBack() {
        val navController = findNavController()
        val action = EditTypeFragmentDirections.actionHurensohn2ToHurensohn() // TODO do bruchts e argument i ha gad kei lust uf das
        navController.navigate(action)
    }

    fun save() {
        Type.removeType(type!!.getID())
        type!!.setName(binding.typeNameEditText.text.toString())
        type!!.setECTSNeeded(binding.neededEctsInput.text.toString().toInt())
        Type.addType(type!!)
        redirectBack()
    }

    fun delete() {
        Type.removeType(type!!.getID())
        redirectBack()
    }
}