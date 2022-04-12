package com.ineedyourcode.nasarog.view.ui.features.sharedelementtransition

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.ineedyourcode.nasarog.R
import com.ineedyourcode.nasarog.databinding.FragmentFeaturesSharedElementTransitionBinding
import com.ineedyourcode.nasarog.utils.*
import com.ineedyourcode.nasarog.view.basefragment.BaseFragment

//TODO:
// - поправить загрузку видео на этот экран
// - добавить shared element transition


class SharedElementTransitionFragment :
    BaseFragment<FragmentFeaturesSharedElementTransitionBinding>(
        FragmentFeaturesSharedElementTransitionBinding::inflate
    ) {

    private val apodExampleViewModel by viewModels<SharedElementTransitionViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {

            apodExampleViewModel.getLiveDataToday().observe(viewLifecycleOwner) {
                renderDataToday(it)
            }

            apodExampleViewModel.getLiveDataYesterday().observe(viewLifecycleOwner) {
                renderDataYesterday(it)
            }

            apodExampleViewModel.getLiveDataBeforeYesterday().observe(viewLifecycleOwner) {
                renderDataBeforeYesterday(it)
            }

            apodExampleViewModel.getApod()
        }
    }

    private fun renderDataToday(state: SharedElementTransitionState) = with(binding) {
        when (state) {
            SharedElementTransitionState.TodayLoading -> {
                setVisibilityOnStateLoading(
                    progressBarToday,
                    cardApodExampleToday
                )
            }

            is SharedElementTransitionState.TodaySuccess -> {
                tvApodExampleTodayDate.text = getCurrentDate()
                tvApodExampleTodayTitle.text = state.apodToday.title
                ivApodExampleToday.loadWithTransformAndCallback(state.apodToday.hdurl, 200) {
                    setVisibilityOnStateSuccess(progressBarToday, cardApodExampleToday)
                    ivApodExampleToday.setOnClickListener {
                        parentFragmentManager
                            .beginTransaction()
                            .add(
                                R.id.navigation_container,
                                SharedElementTransitionDetailsFragment.newInstance(
                                    tvApodExampleTodayDate.text.toString(),
                                    tvApodExampleTodayTitle.text.toString(),
                                    state.apodToday.explanation,
                                    state.apodToday.hdurl
                                )
                            )
                            .addToBackStack("")
                            .commit()
                    }
                }
            }

            is SharedElementTransitionState.TodayError -> {
                view?.showSnackWithoutAction(state.error.message.toString())
            }

            else -> {}
        }
    }

    private fun renderDataYesterday(state: SharedElementTransitionState) = with(binding) {
        when (state) {
            SharedElementTransitionState.YesterdayLoading -> {
                setVisibilityOnStateLoading(
                    progressBarYesterday,
                    cardApodExampleYesterday
                )
            }

            is SharedElementTransitionState.YesterdaySuccess -> {
                tvApodExampleYesterdayDate.text = getYesterdayDate()
                tvApodExampleYesterdayTitle.text = state.apodYesterday.title
                ivApodExampleYesterday.loadWithTransformAndCallback(
                    state.apodYesterday.hdurl,
                    200
                ) {
                    ivApodExampleYesterday.setOnClickListener {
                        parentFragmentManager
                            .beginTransaction()
                            .add(
                                R.id.navigation_container,
                                SharedElementTransitionDetailsFragment.newInstance(
                                    tvApodExampleYesterdayDate.text.toString(),
                                    tvApodExampleYesterdayTitle.text.toString(),
                                    state.apodYesterday.explanation,
                                    state.apodYesterday.hdurl
                                )
                            )
                            .addToBackStack("")
                            .commit()
                    }
                    setVisibilityOnStateSuccess(
                        progressBarYesterday,
                        cardApodExampleYesterday
                    )
                }
            }

            is SharedElementTransitionState.BeforeYesterdayError -> {
                view?.showSnackWithoutAction(state.error.message.toString())
            }

            else -> {}
        }
    }

    private fun renderDataBeforeYesterday(state: SharedElementTransitionState) = with(binding) {
        when (state) {
            SharedElementTransitionState.BeforeYesterdayLoading -> {
                setVisibilityOnStateLoading(
                    progressBarBeforeYesterday,
                    cardApodExampleBeforeYesterday
                )
            }

            is SharedElementTransitionState.BeforeYesterdaySuccess -> {
                tvApodExampleBeforeYesterdayDate.text = getBeforeYesterdayDate()
                tvApodExampleBeforeYesterdayTitle.text = state.apodBeforeYesterday.title
                ivApodExampleBeforeYesterday.loadWithTransformAndCallback(
                    state.apodBeforeYesterday.hdurl,
                    200
                ) {
                    ivApodExampleBeforeYesterday.setOnClickListener {
                        parentFragmentManager
                            .beginTransaction()
                            .add(
                                R.id.navigation_container,
                                SharedElementTransitionDetailsFragment.newInstance(
                                    tvApodExampleBeforeYesterdayDate.text.toString(),
                                    tvApodExampleBeforeYesterdayTitle.text.toString(),
                                    state.apodBeforeYesterday.explanation,
                                    state.apodBeforeYesterday.hdurl
                                )
                            )
                            .addToBackStack("")
                            .commit()
                    }

                    setVisibilityOnStateSuccess(
                        progressBarBeforeYesterday,
                        cardApodExampleBeforeYesterday
                    )
                }
            }

            is SharedElementTransitionState.YesterdayError -> {
                view?.showSnackWithoutAction(state.error.message.toString())
            }

            else -> {}
        }
    }
}