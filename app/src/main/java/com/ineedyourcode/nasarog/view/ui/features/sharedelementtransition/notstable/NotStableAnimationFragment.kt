package com.ineedyourcode.nasarog.view.ui.features.sharedelementtransition.notstable

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ineedyourcode.nasarog.databinding.FragmentFeaturesSharedElementTransitionBinding
import com.ineedyourcode.nasarog.utils.*
import com.ineedyourcode.nasarog.view.basefragment.BaseFragment
import com.ineedyourcode.nasarog.view.ui.features.FeaturesListFragment
import com.ineedyourcode.nasarog.view.ui.features.sharedelementtransition.stable.SharedElementTransitionState

/**
 * Фрагмент для демонстрации поведения observer, при котором приходят не все данные из liveData
 */

class NotStableAnimationFragment :
    BaseFragment<FragmentFeaturesSharedElementTransitionBinding>(FragmentFeaturesSharedElementTransitionBinding::inflate) {

    private val notStableViewModel by viewModels<NotStableAnimationViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notStableViewModel.getLiveData().observe(viewLifecycleOwner) {
            renderDataToday(it)
        }

        notStableViewModel.getApod()

        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            FeaturesListFragment.KEY_BACK_STACK_ENTRY, true
        )
    }

    private fun renderDataToday(state: SharedElementTransitionState) = with(binding) {
        when (state) {
            SharedElementTransitionState.TodayLoading -> {
                setVisibilityOnStateLoading(
                    progressBarToday,
                    cardApodExampleToday
                )
            }

            SharedElementTransitionState.YesterdayLoading -> {
                setVisibilityOnStateLoading(
                    progressBarYesterday,
                    cardApodExampleYesterday
                )
            }

            SharedElementTransitionState.BeforeYesterdayLoading -> {
                setVisibilityOnStateLoading(
                    progressBarBeforeYesterday,
                    cardApodExampleBeforeYesterday
                )
            }

            is SharedElementTransitionState.TodaySuccess -> {
                tvApodExampleTodayDate.text = getCurrentDate()
                tvApodExampleTodayTitle.text = state.apodToday.title
                ivApodExampleToday.loadWithTransformAndCallback(state.apodToday.hdurl, 200) {
                    setVisibilityOnStateSuccess(progressBarToday, cardApodExampleToday)
                }
            }

            is SharedElementTransitionState.YesterdaySuccess -> {
                tvApodExampleYesterdayDate.text = getYesterdayDate()
                tvApodExampleYesterdayTitle.text = state.apodYesterday.title
                ivApodExampleYesterday.loadWithTransformAndCallback(
                    state.apodYesterday.hdurl,
                    200
                ) {
                    setVisibilityOnStateSuccess(
                        progressBarYesterday,
                        cardApodExampleYesterday
                    )
                }
            }

            is SharedElementTransitionState.BeforeYesterdaySuccess -> {
                tvApodExampleBeforeYesterdayDate.text = getBeforeYesterdayDate()
                tvApodExampleBeforeYesterdayTitle.text = state.apodBeforeYesterday.title
                ivApodExampleBeforeYesterday.loadWithTransformAndCallback(
                    state.apodBeforeYesterday.hdurl,
                    200
                ) {
                    setVisibilityOnStateSuccess(
                        progressBarBeforeYesterday,
                        cardApodExampleBeforeYesterday
                    )
                }
            }

            is SharedElementTransitionState.TodayError -> {
                view?.showSnackWithoutAction(state.error.message.toString())
            }

            is SharedElementTransitionState.YesterdayError -> {
                view?.showSnackWithoutAction(state.error.message.toString())
            }

            is SharedElementTransitionState.BeforeYesterdayError -> {
                view?.showSnackWithoutAction(state.error.message.toString())
            }
        }
    }
}