package com.ineedyourcode.nasarog.view.ui.features.recyclerview

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ineedyourcode.nasarog.databinding.FragmentFeaturesRecyclerViewBinding
import com.ineedyourcode.nasarog.model.dto.asteroidsdto.AsteroidListDto
import com.ineedyourcode.nasarog.utils.setVisibilityOnStateLoading
import com.ineedyourcode.nasarog.utils.setVisibilityOnStateSuccess
import com.ineedyourcode.nasarog.utils.showToast
import com.ineedyourcode.nasarog.view.basefragment.BaseFragment

class RecyclerViewFragment :
    BaseFragment<FragmentFeaturesRecyclerViewBinding>(FragmentFeaturesRecyclerViewBinding::inflate) {

    private val viewModel by viewModels<RecyclerViewViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = RecyclerViewFragmentAdapter(object : OnAsteroidItemClickListener {
            override fun onAsteroidItemClick(asteroid: AsteroidListDto.AsteroidDto) {
                showToast(requireContext(), asteroid.name)
            }
        })

        viewModel.getLiveData().observe(viewLifecycleOwner) {
            renderData(it, adapter)
        }

        viewModel.getAsteroidsDataRequest()
    }

    private fun renderData(state: AsteroidDataState, mAdapter: RecyclerViewFragmentAdapter) {
        with(binding) {
            when (state) {
                AsteroidDataState.Loading -> {
                    setVisibilityOnStateLoading(recyclerViewSpinKit, recyclerView)
                }
                is AsteroidDataState.AsteroidDataSuccess -> {
                    mAdapter.setData(state.asteroidList)
                    recyclerView.apply {
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = mAdapter
                    }
                    setVisibilityOnStateSuccess(recyclerViewSpinKit, recyclerView)
                }
                is AsteroidDataState.Error -> {}
            }
        }
    }
}