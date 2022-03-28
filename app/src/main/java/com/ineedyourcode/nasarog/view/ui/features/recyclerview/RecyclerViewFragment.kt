package com.ineedyourcode.nasarog.view.ui.features.recyclerview

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ineedyourcode.nasarog.R
import com.ineedyourcode.nasarog.databinding.FragmentFeaturesRecyclerViewBinding
import com.ineedyourcode.nasarog.model.dto.asteroidsdto.AsteroidListDto
import com.ineedyourcode.nasarog.utils.*
import com.ineedyourcode.nasarog.view.basefragment.BaseFragment

class RecyclerViewFragment :
    BaseFragment<FragmentFeaturesRecyclerViewBinding>(FragmentFeaturesRecyclerViewBinding::inflate) {

    private val viewModel by viewModels<RecyclerViewViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getLiveData().observe(viewLifecycleOwner) {
            renderData(it)
        }

        val dateStart = getCurrentDate()
        val dateEnd = getTwoDaysForwardDate()

        viewModel.getAsteroidsDataRequest(
            dateStart,
            dateEnd,
            getString(
                R.string.header,
                convertNasaDateFormatToMyFormat(dateStart),
                convertNasaDateFormatToMyFormat(dateEnd)
            )
        )
    }

    private fun renderData(state: AsteroidDataState) {
        with(binding) {
            when (state) {
                AsteroidDataState.Loading -> {
                    setVisibilityOnStateLoading(recyclerViewSpinKit)
                }
                is AsteroidDataState.AsteroidDataSuccess -> {
                    recyclerView.apply {
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = RecyclerViewFragmentAdapter(object : OnAsteroidItemClickListener {
                            override fun onAsteroidItemClick(asteroid: AsteroidListDto.AsteroidDto) {
                                showToast(requireContext(), asteroid.name)
                            }
                        }).apply {
                            setData(state.asteroidList)
                        }
                    }
                    setVisibilityOnStateSuccess(recyclerViewSpinKit)
                }
                is AsteroidDataState.Error -> {}
            }
        }
    }
}