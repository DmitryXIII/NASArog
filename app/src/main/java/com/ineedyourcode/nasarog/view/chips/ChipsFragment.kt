package com.ineedyourcode.nasarog.view.chips

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.ineedyourcode.nasarog.databinding.FragmentChipsBinding
import com.ineedyourcode.nasarog.view.BaseBindingFragment

class ChipsFragment : BaseBindingFragment<FragmentChipsBinding>(FragmentChipsBinding::inflate) {

    companion object {
        fun newInstance() = ChipsFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.chipGroup.setOnCheckedChangeListener { _, checkedId ->
            Toast.makeText(requireContext(), "chip $checkedId", Toast.LENGTH_SHORT).show()
        }
    }
}