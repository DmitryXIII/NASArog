package com.ineedyourcode.nasarog.view.ui.features.ux

import android.os.Bundle
import android.view.View
import com.ineedyourcode.nasarog.R
import com.ineedyourcode.nasarog.databinding.FragmentUxTextBinding
import com.ineedyourcode.nasarog.view.basefragment.BaseFragment
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity

class UXTextFragment : BaseFragment<FragmentUxTextBinding>(FragmentUxTextBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showGuide()
    }

    private fun showGuide() {
        showGuideScene1()
    }

    private fun showGuideScene1() {
        GuideView.Builder(requireContext())
            .setTitle(getString(R.string.ux_guide_title_old_school))
            .setContentText(getString(R.string.ux_text_guide_scene_1_content))
            .setGravity(Gravity.auto)
            .setTargetView(binding.oldSchoolDesignContainer)
            .setDismissType(DismissType.anywhere)
            .setGuideListener { showGuideScene2() }
            .build()
            .show()
    }

    private fun showGuideScene2() {
        GuideView.Builder(requireContext())
            .setTitle(getString(R.string.ux_guide_title_modern))
            .setContentText(getString(R.string.ux_text_guide_scene_2_content))
            .setGravity(Gravity.auto)
            .setTargetView(binding.modernDesignContainer)
            .setDismissType(DismissType.anywhere)
            .setGuideListener {  }
            .build()
            .show()
    }
}