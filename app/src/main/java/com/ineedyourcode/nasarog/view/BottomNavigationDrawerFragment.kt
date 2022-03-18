package com.ineedyourcode.nasarog.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.ineedyourcode.nasarog.R
import com.ineedyourcode.nasarog.databinding.FragmentBottomNavigationDrawerBinding
import com.ineedyourcode.nasarog.view.basefragment.BaseBottomSheetDialogFragment

class BottomNavigationDrawerFragment : BaseBottomSheetDialogFragment<FragmentBottomNavigationDrawerBinding>(FragmentBottomNavigationDrawerBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bottomNavigationMenuLayout.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_menu_bottomnav_themes -> {
                    findNavController().navigate(R.id.action_bottomNavigationDrawerFragment_to_settingsFragment)
                    dismiss()
                    true
                }
                R.id.action_menu_bottomnav_two -> {
                    Toast.makeText(requireContext(), "CLICK TWO", Toast.LENGTH_SHORT).show()
                    dismiss()
                    true
                }
                else -> true
            }
        }
    }
}