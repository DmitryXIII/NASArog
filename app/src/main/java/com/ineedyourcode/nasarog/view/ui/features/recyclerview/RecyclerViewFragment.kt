package com.ineedyourcode.nasarog.view.ui.features.recyclerview

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.ineedyourcode.nasarog.databinding.FragmentFeaturesRecyclerViewBinding
import com.ineedyourcode.nasarog.utils.setVisibilityOnStateLoading
import com.ineedyourcode.nasarog.utils.setVisibilityOnStateSuccess
import com.ineedyourcode.nasarog.view.basefragment.BaseFragment

class RecyclerViewFragment :
    BaseFragment<FragmentFeaturesRecyclerViewBinding>(FragmentFeaturesRecyclerViewBinding::inflate) {

    private val viewModel by viewModels<RecyclerViewViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getLiveData().observe(viewLifecycleOwner) {
            renderData(it)
        }

        viewModel.getAsteroidsDataRequest()
    }

    private fun renderData(state: AsteroidDataState) {
        with(binding) {
            when (state) {
                AsteroidDataState.Loading -> {
                    setVisibilityOnStateLoading(recyclerViewSpinKit, text)
                }
                is AsteroidDataState.AsteroidDataSuccess -> {
                    text.text = state.toString()
                    setVisibilityOnStateSuccess(recyclerViewSpinKit, text)
                }
                is AsteroidDataState.Error -> {}
            }
        }
    }
}