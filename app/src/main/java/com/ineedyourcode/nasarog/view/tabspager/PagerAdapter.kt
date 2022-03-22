package com.ineedyourcode.nasarog.view.tabspager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ineedyourcode.nasarog.view.tabspager.tabs.earthphoto.TabEarthPhotoFragment
import com.ineedyourcode.nasarog.view.tabspager.tabs.marsphotos.TabMarsPhotoFragment
import com.ineedyourcode.nasarog.view.tabspager.tabs.apod.TabApodFragment

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