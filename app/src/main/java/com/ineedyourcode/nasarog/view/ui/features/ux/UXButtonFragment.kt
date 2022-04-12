package com.ineedyourcode.nasarog.view.ui.features.ux

import android.os.Bundle
import android.view.View
import com.ineedyourcode.nasarog.R
import com.ineedyourcode.nasarog.databinding.FragmentUxButtonBinding
import com.ineedyourcode.nasarog.view.basefragment.BaseFragment
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity

class UXButtonFragment : BaseFragment<FragmentUxButtonBinding>(FragmentUxButtonBinding::inflate) {

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
            .setContentText(getString(R.string.ux_button_guide_scene_1_content))
            .setGravity(Gravity.auto)
            .setTargetView(binding.oldSchoolDesignContainer)
            .setDismissType(DismissType.anywhere)
            .setGuideListener { showGuideScene2() }
            .build()
            .show()
    }

    private fun showGuideScene2() {
        GuideView.Builder(requireContext())
            .setTitle(getString(R.string.ux_guide_title_old_school))
            .setContentText(getString(R.string.ux_button_guide_scene_2_content))
            .setGravity(Gravity.auto)
            .setTargetView(binding.btnUxOldSchoolButtonPriceYear)
            .setDismissType(DismissType.anywhere)
            .setGuideListener { showGuideScene3() }
            .build()
            .show()
    }

    private fun showGuideScene3() {
        GuideView.Builder(requireContext())
            .setTitle(getString(R.string.ux_guide_title_modern))
            .setContentText(getString(R.string.ux_button_guide_scene_3_content))
            .setGravity(Gravity.auto)
            .setTargetView(binding.modernDesignContainer)
            .setDismissType(DismissType.anywhere)
            .setGuideListener { showGuideScene4() }
            .build()
            .show()
    }

    private fun showGuideScene4() {
        GuideView.Builder(requireContext())
            .setTitle(getString(R.string.ux_guide_title_modern))
            .setContentText(getString(R.string.ux_button_guide_scene_4_content))
            .setGravity(Gravity.auto)
            .setTargetView(binding.btnUxModernButtonPriceYear)
            .setDismissType(DismissType.anywhere)
            .setGuideListener { showGuideScene5() }
            .build()
            .show()
    }

    private fun showGuideScene5() {
        GuideView.Builder(requireContext())
            .setTitle(getString(R.string.ux_guide_title_modern))
            .setContentText(getString(R.string.ux_button_guide_scene_5_content))
            .setGravity(Gravity.auto)
            .setTargetView(binding.btnUxModernButtonLicense)
            .setDismissType(DismissType.anywhere)
            .build()
            .show()
    }
}