package com.ineedyourcode.nasarog.view.sharedtransition

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.ineedyourcode.nasarog.R
import com.ineedyourcode.nasarog.databinding.FragmentApodExampleBinding
import com.ineedyourcode.nasarog.utils.*
import com.ineedyourcode.nasarog.view.basefragment.BaseFragment
import kotlin.math.exp

class ApodExampleFragment :
    BaseFragment<FragmentApodExampleBinding>(FragmentApodExampleBinding::inflate) {

    private val apodExampleViewModel by viewModels<ApodExampleViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    private fun renderDataToday(state: ApodExampleState) = with(binding) {
        when (state) {
            ApodExampleState.TodayLoading -> {
                setVisibilityOnStateLoading(
                    progressBarToday,
                    cardApodExampleToday
                )
            }

            is ApodExampleState.TodaySuccess -> {
                tvApodExampleTodayDate.text = getCurrentDate()
                tvApodExampleTodayTitle.text = state.apodToday.title
                ivApodExampleToday.loadWithTransformAndCallback(state.apodToday.hdurl, 200, 0f) {
                    setVisibilityOnStateSuccess(progressBarToday, cardApodExampleToday)
                    navigateWithTransition(
                        ivApodExampleToday,
                        tvApodExampleTodayDate,
                        tvApodExampleTodayTitle,
                        state.apodToday.hdurl,
                        state.apodToday.explanation
                    )
                }
            }

            is ApodExampleState.TodayError -> {
                view?.showSnackWithoutAction(state.error.message.toString())
            }
            else -> {}
        }
    }

    private fun renderDataYesterday(state: ApodExampleState) = with(binding) {
        when (state) {
            ApodExampleState.YesterdayLoading -> {
                setVisibilityOnStateLoading(
                    progressBarYesterday,
                    cardApodExampleYesterday
                )
            }

            is ApodExampleState.YesterdaySuccess -> {
                tvApodExampleYesterdayDate.text = getYesterdayDate()
                tvApodExampleYesterdayTitle.text = state.apodYesterday.title
                ivApodExampleYesterday.loadWithTransformAndCallback(
                    state.apodYesterday.hdurl,
                    200,
                    0f
                ) {
                    navigateWithTransition(
                        ivApodExampleYesterday,
                        tvApodExampleYesterdayDate,
                        tvApodExampleYesterdayTitle,
                        state.apodYesterday.hdurl,
                        state.apodYesterday.explanation
                    )
                    setVisibilityOnStateSuccess(
                        progressBarYesterday,
                        cardApodExampleYesterday
                    )
                }
            }

            is ApodExampleState.BeforeYesterdayError -> {
                view?.showSnackWithoutAction(state.error.message.toString())
            }
            else -> {}
        }
    }

    private fun renderDataBeforeYesterday(state: ApodExampleState) = with(binding) {
        when (state) {
            ApodExampleState.BeforeYesterdayLoading -> {
                setVisibilityOnStateLoading(
                    progressBarBeforeYesterday,
                    cardApodExampleBeforeYesterday
                )
            }

            is ApodExampleState.BeforeYesterdaySuccess -> {
                tvApodExampleBeforeYesterdayDate.text = getBeforeYesterdayDate()
                tvApodExampleBeforeYesterdayTitle.text = state.apodBeforeYesterday.title
                ivApodExampleBeforeYesterday.loadWithTransformAndCallback(
                    state.apodBeforeYesterday.hdurl,
                    200,
                    0f
                ) {
                    navigateWithTransition(
                        ivApodExampleBeforeYesterday,
                        tvApodExampleBeforeYesterdayDate,
                        tvApodExampleBeforeYesterdayTitle,
                        state.apodBeforeYesterday.hdurl,
                        state.apodBeforeYesterday.explanation,
                        postponed = true
                    )
                    setVisibilityOnStateSuccess(
                        progressBarBeforeYesterday,
                        cardApodExampleBeforeYesterday
                    )
                }
            }

            is ApodExampleState.YesterdayError -> {
                view?.showSnackWithoutAction(state.error.message.toString())
            }
            else -> {}
        }
    }

    private fun navigateWithTransition(
        image: ImageView,
        date: TextView,
        title: TextView,
        hdurl: String,
        explanation: String,
        postponed: Boolean = false,
        explanationView: TextView = binding.tvEmptyExplanation
    ) {
        image.setOnClickListener {
            val extras = FragmentNavigatorExtras(
                image to "transition_image_big",
                date to "transition_details_date_big",
                title to "transition_details_title_big",
                explanationView to "transition_details_explanation_big"
            )

            findNavController().navigate(
                R.id.action_apodExampleFragment_to_apodExampleDetailsFragment,
                bundleOf(
                    ApodExampleDetailsFragment.KEY_DATE to date.text,
                    ApodExampleDetailsFragment.KEY_APOD_TITLE to title.text,
                    ApodExampleDetailsFragment.KEY_APOD_URL to hdurl,
                    ApodExampleDetailsFragment.KEY_APOD_EXPLANATION to explanation,
                    ApodExampleDetailsFragment.KEY_IS_POSTPONED_TRANSITION to postponed
                ),
                null,
                extras
            )
        }
    }
}