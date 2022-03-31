package com.ineedyourcode.nasarog.view.ui.features

import android.os.Bundle
import android.view.View
import com.ineedyourcode.nasarog.R
import com.ineedyourcode.nasarog.databinding.FragmentFeaturesListBinding
import com.ineedyourcode.nasarog.utils.showToast
import com.ineedyourcode.nasarog.view.basefragment.BaseFragment
import com.ineedyourcode.nasarog.view.ui.features.custombehavior.CoordinatorLayoutExampleFragment
import com.ineedyourcode.nasarog.view.ui.features.recyclerview.RecyclerViewFragment

class FeaturesListFragment :
    BaseFragment<FragmentFeaturesListBinding>(FragmentFeaturesListBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCustomBehavior.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .add(R.id.navigation_container, CoordinatorLayoutExampleFragment())
                .addToBackStack("")
                .commit()
        }

        binding.btnSharedElementTransition.setOnClickListener {
            showToast(requireContext(), "Временно отключено")
        }

        binding.btnRecyclerView.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .add(R.id.navigation_container, RecyclerViewFragment())
                .addToBackStack("")
                .commit()
        }
    }
}