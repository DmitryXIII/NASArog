package com.ineedyourcode.nasarog.view.ui.tabspager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ineedyourcode.nasarog.view.ui.tabspager.tabs.apod.TabApodFragment
import com.ineedyourcode.nasarog.view.ui.tabspager.tabs.earthphoto.TabEarthPhotoFragment
import com.ineedyourcode.nasarog.view.ui.tabspager.tabs.marsphotos.TabMarsPhotoFragment

class PagerAdapter(fragmentManager: FragmentActivity): FragmentStateAdapter(fragmentManager) {

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> TabApodFragment.newInstance()
            1 -> TabEarthPhotoFragment()
            2 -> TabMarsPhotoFragment()
            else -> Fragment()
        }
    }
}