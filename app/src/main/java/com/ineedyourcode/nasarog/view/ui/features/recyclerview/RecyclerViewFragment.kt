package com.ineedyourcode.nasarog.view.ui.features.recyclerview

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.ineedyourcode.nasarog.databinding.FragmentFeaturesRecyclerViewBinding
import com.ineedyourcode.nasarog.view.basefragment.BaseFragment

class RecyclerViewFragment: BaseFragment<FragmentFeaturesRecyclerViewBinding>(FragmentFeaturesRecyclerViewBinding::inflate) {

    private val viewModel by viewModels<RecyclerViewViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getLiveData().observe(viewLifecycleOwner) {
            renderData(it)
        }

        viewModel.getAsteroidsDataRequest()
    }

    private fun renderData(state: AsteroidDataState) {
        when(state){
            is AsteroidDataState.AsteroidDataSuccess -> {
binding.text.text = state.toString()
            }
            is AsteroidDataState.Error -> {}
            AsteroidDataState.Loading -> {}
        }
    }
}