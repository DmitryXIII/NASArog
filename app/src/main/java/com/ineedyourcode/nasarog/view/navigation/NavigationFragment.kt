package com.ineedyourcode.nasarog.view.navigation

import android.os.Bundle
import android.view.View
import com.ineedyourcode.nasarog.R
import com.ineedyourcode.nasarog.databinding.FragmentNavigationBinding
import com.ineedyourcode.nasarog.utils.showSnackWithAction
import com.ineedyourcode.nasarog.view.BaseBindingFragment
import com.ineedyourcode.nasarog.view.BottomNavigationDrawerFragment
import com.ineedyourcode.nasarog.view.settings.SettingsFragment
import com.ineedyourcode.nasarog.view.tabs.TabFragment

class NavigationFragment :
    BaseBindingFragment<FragmentNavigationBinding>(FragmentNavigationBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            childFragmentManager
                .beginTransaction()
                .replace(R.id.navigation_container, TabFragment())
                .commit()
        }

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_bottom_nav_view_apod -> {
                    if (!it.isChecked) {
                        childFragmentManager
                            .beginTransaction()
                            .replace(R.id.navigation_container, TabFragment())
                            .commit()
                    }
                    true
                }
                R.id.action_bottom_navigation_view_open_menu -> {
                    BottomNavigationDrawerFragment().show(requireActivity().supportFragmentManager, "")
                    true
                }
                else -> {
                    view.showSnackWithAction("111111111111111", "22222222") {}
                    true
                }
            }
        }
    }
}