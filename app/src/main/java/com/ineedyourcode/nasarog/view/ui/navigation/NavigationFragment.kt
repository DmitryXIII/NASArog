package com.ineedyourcode.nasarog.view.ui.navigation

import android.os.Bundle
import android.view.View
import com.ineedyourcode.nasarog.R
import com.ineedyourcode.nasarog.databinding.FragmentNavigationBinding
import com.ineedyourcode.nasarog.view.basefragment.BaseFragment
import com.ineedyourcode.nasarog.view.ui.bottomnavdrawer.BottomNavigationDrawerFragment
import com.ineedyourcode.nasarog.view.ui.features.FeaturesListFragment
import com.ineedyourcode.nasarog.view.ui.tabspager.TabsPagerFragment

class NavigationFragment :
    BaseFragment<FragmentNavigationBinding>(FragmentNavigationBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            childFragmentManager
                .beginTransaction()
                .replace(R.id.navigation_container, TabsPagerFragment())
                .commit()
        }

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.actionTabsPagerFragment -> {
                    if (!it.isChecked) {
                        parentFragmentManager
                            .beginTransaction()
                            .replace(R.id.navigation_container, TabsPagerFragment())
                            .commit()
                    }
                    true
                }
                R.id.actionSettings -> {
                    BottomNavigationDrawerFragment().show(
                        parentFragmentManager,
                        ""
                    )
                    true
                }
                R.id.actionFeaturesListFragment -> {
                    if (!it.isChecked) {
                        parentFragmentManager
                            .beginTransaction()
                            .replace(R.id.navigation_container, FeaturesListFragment())
                            .commit()
                    }
                    true
                }
                else -> false
            }
        }
    }
}