package com.ineedyourcode.nasarog.view.ui.features.recyclerview

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private lateinit var recViewLayoutManager : LinearLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recViewLayoutManager = LinearLayoutManager(requireContext())

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
            recyclerViewFragmentAdapter.appendItem()
            binding.recyclerView.smoothScrollToPosition(recyclerViewFragmentAdapter.itemCount)
        }
    }

    private fun renderData(state: AsteroidDataState) {
        with(binding) {
            when (state) {
                AsteroidDataState.Loading -> {
                    setVisibilityOnStateLoading(recyclerViewSpinKit, recyclerView, fabAddItem)
                }
                is AsteroidDataState.AsteroidDataSuccess -> {
                    recyclerView.apply {
                        layoutManager = recViewLayoutManager
                        adapter = recyclerViewFragmentAdapter.apply {
                            setData(state.asteroidList)
                        }
//                        addOnScrollListener(object : RecyclerView.OnScrollListener() {
//                            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                                recyclerViewFragmentAdapter.getOnScreenPosition(recViewLayoutManager.findFirstVisibleItemPosition(), recViewLayoutManager.findLastVisibleItemPosition())
//                            }
//                        })
                    }
                    setVisibilityOnStateSuccess(recyclerViewSpinKit, recyclerView, fabAddItem)
                }
                is AsteroidDataState.Error -> {}
            }
        }
    }
}