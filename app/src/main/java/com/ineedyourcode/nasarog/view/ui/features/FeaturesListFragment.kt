package com.ineedyourcode.nasarog.view.ui.features

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.ineedyourcode.nasarog.R
import com.ineedyourcode.nasarog.databinding.FragmentFeaturesListBinding
import com.ineedyourcode.nasarog.view.basefragment.BaseFragment

class FeaturesListFragment : BaseFragment<FragmentFeaturesListBinding>(FragmentFeaturesListBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCustomBehavior.setOnClickListener {
            findNavController().navigate(R.id.action_featuresListFragment_to_coordinatorLayoutExampleFragment)
        }

        binding.btnSharedElementTransition.setOnClickListener {
            findNavController().navigate(R.id.action_featuresListFragment_to_sharedElementTransitionFragment)
        }

        binding.btnSharedElementTransitionNotStable.setOnClickListener {
            findNavController().navigate(R.id.action_featuresListFragment_to_notStableAnimationFragment)
        }
    }
}