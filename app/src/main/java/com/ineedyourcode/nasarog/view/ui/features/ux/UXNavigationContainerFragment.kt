package com.ineedyourcode.nasarog.view.ui.features.ux

import android.os.Bundle
import android.view.View
import com.ineedyourcode.nasarog.R
import com.ineedyourcode.nasarog.databinding.FragmentUxNavigationContainerBinding
import com.ineedyourcode.nasarog.view.basefragment.BaseFragment

class UXNavigationContainerFragment :
    BaseFragment<FragmentUxNavigationContainerBinding>(FragmentUxNavigationContainerBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            childFragmentManager
                .beginTransaction()
                .replace(R.id.UXNavigationContainer, UXTextFragment())
                .commit()
        }

        binding.UXBottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.actionUXTextFragment -> {
                    if (!it.isChecked) {
                        childFragmentManager
                            .beginTransaction()
                            .replace(R.id.UXNavigationContainer, UXTextFragment())
                            .commit()
                    }
                    true
                }

                R.id.actionUXButtonFragment -> {
                    if (!it.isChecked) {
                        childFragmentManager
                            .beginTransaction()
                            .replace(R.id.UXNavigationContainer, UXButtonFragment())
                            .commit()
                    }
                    true
                }
                else -> false
            }
        }
    }
}