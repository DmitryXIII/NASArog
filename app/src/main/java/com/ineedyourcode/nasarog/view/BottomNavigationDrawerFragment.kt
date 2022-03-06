package com.ineedyourcode.nasarog.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ineedyourcode.nasarog.R
import com.ineedyourcode.nasarog.databinding.FragmentBottomNavigationDrawerBinding
import com.ineedyourcode.nasarog.view.settings.SettingsFragment

class BottomNavigationDrawerFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentBottomNavigationDrawerBinding? = null
    private val binding: FragmentBottomNavigationDrawerBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomNavigationDrawerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bottomNavigationMenuLayout.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_menu_bottomnav_one -> {
                    parentFragmentManager
                        .beginTransaction()
                        .replace(R.id.navigation_container, SettingsFragment.newInstance())
                        .commit()
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