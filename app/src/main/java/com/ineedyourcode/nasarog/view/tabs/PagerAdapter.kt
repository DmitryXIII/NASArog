package com.ineedyourcode.nasarog.view.tabs

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ineedyourcode.nasarog.view.sharedtransition.ApodExampleFragment
import com.ineedyourcode.nasarog.view.tabs.photo.earthphoto.EarthPhotoFragment
import com.ineedyourcode.nasarog.view.tabs.photo.marsphotos.MarsPhotoFragment
import com.ineedyourcode.nasarog.view.tabs.photo.picoftheday.PictureOfTheDayFragment

class PagerAdapter(fragmentManager: FragmentActivity): FragmentStateAdapter(fragmentManager) {

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> PictureOfTheDayFragment.newInstance()
            1 -> EarthPhotoFragment()
            2 -> MarsPhotoFragment()
            else -> Fragment()
        }
    }
}