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
import java.util.*
import kotlin.random.Random

class RecyclerViewFragment :
    BaseFragment<FragmentFeaturesRecyclerViewBinding>(FragmentFeaturesRecyclerViewBinding::inflate) {

    private val viewModel by viewModels<RecyclerViewViewModel>()

    private lateinit var recyclerViewFragmentAdapter: RecyclerViewFragmentAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewFragmentAdapter =
            RecyclerViewFragmentAdapter(object : OnAsteroidItemClickListener {
                override fun onAsteroidItemClick(asteroid: AsteroidListDto.AsteroidDto) {
                    showToast(requireContext(), asteroid.name)
                }
            })

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

        binding.fabAddItem.setOnClickListener {
            recyclerViewFragmentAdapter.appendItem(generateAsteroidItem())
            binding.recyclerView.smoothScrollToPosition(recyclerViewFragmentAdapter.itemCount)
        }
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
                        adapter = recyclerViewFragmentAdapter.apply {
                            setData(state.asteroidList)
                        }
                    }
                    setVisibilityOnStateSuccess(recyclerViewSpinKit)
                }
                is AsteroidDataState.Error -> {}
            }
        }
    }

    private fun generateAsteroidItem(): AsteroidListDto.AsteroidDto {
        val generatedIsPotentiallyHazardousAsteroid = Random.nextBoolean()

        return AsteroidListDto.AsteroidDto(
            id = UUID.randomUUID().toString(),
            name = "Кастомный астероид",
            absoluteMagnitudeH = (((Random.nextFloat() * 20) + 5).toString().substring(0, 5)
                .toFloat()),
            isPotentiallyHazardousAsteroid = generatedIsPotentiallyHazardousAsteroid,
            closeApproachData = listOf(
                AsteroidListDto.AsteroidDto.CloseApproachData(getCurrentDate())
            ),
            type = when (generatedIsPotentiallyHazardousAsteroid) {
                true -> 1
                false -> 2
            }
        )
    }
}