package com.ineedyourcode.nasarog.view.ui.tabspager

import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import com.ineedyourcode.nasarog.R
import com.ineedyourcode.nasarog.databinding.FragmentTabsPagerBinding
import com.ineedyourcode.nasarog.view.basefragment.BaseFragment

class TabsPagerFragment: BaseFragment<FragmentTabsPagerBinding>(FragmentTabsPagerBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager2.adapter = PagerAdapter(requireActivity())
        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = when(position){
                0 -> getString(R.string.tab_title_apod)
                1 -> getString(R.string.tab_title_earth_photo)
                2 -> getString(R.string.tab_title_mars_photo)
                else -> ""
            }
        }.attach()
    }
}