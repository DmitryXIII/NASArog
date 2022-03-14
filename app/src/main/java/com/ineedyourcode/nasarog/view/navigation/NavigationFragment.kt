package com.ineedyourcode.nasarog.view.navigation

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.ineedyourcode.nasarog.R
import com.ineedyourcode.nasarog.databinding.FragmentNavigationBinding
import com.ineedyourcode.nasarog.view.BaseBindingFragment

class NavigationFragment :
    BaseBindingFragment<FragmentNavigationBinding>(FragmentNavigationBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        NavigationUI.setupWithNavController(
            binding.bottomNavigationView,
            (childFragmentManager.findFragmentById(R.id.navigation_container) as NavHostFragment).navController
        )
    }
}