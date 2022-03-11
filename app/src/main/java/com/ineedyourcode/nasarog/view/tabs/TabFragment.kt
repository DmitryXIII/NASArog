package com.ineedyourcode.nasarog.view.tabs

import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import com.ineedyourcode.nasarog.databinding.FragmentTabsBinding
import com.ineedyourcode.nasarog.view.BaseBindingFragment

class TabFragment: BaseBindingFragment<FragmentTabsBinding>(FragmentTabsBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager2.adapter = PagerAdapter(requireActivity())
        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = when(position){
                0 -> "Фото дня"
                1 -> "Фото Земли"
                2 -> "Фото Марса"
                else -> ""
            }
        }.attach()
    }
}