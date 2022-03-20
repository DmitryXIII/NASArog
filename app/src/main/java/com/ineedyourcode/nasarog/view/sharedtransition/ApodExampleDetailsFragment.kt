package com.ineedyourcode.nasarog.view.sharedtransition

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import coil.load
import com.ineedyourcode.nasarog.databinding.FragmentApodExampleDetailsBinding
import com.ineedyourcode.nasarog.utils.showSnackWithoutAction
import com.ineedyourcode.nasarog.utils.showToast
import com.ineedyourcode.nasarog.view.basefragment.BaseFragment

class ApodExampleDetailsFragment :
    BaseFragment<FragmentApodExampleDetailsBinding>(FragmentApodExampleDetailsBinding::inflate) {

    companion object {
        const val KEY_DATE = "KEY_DATE"
        const val KEY_APOD_URL = "KEY_APOD_URL"
        const val KEY_APOD_TITLE = "KEY_APOD_TITLE"
        const val KEY_APOD_EXPLANATION = "KEY_APOD_EXPLANATION"
        const val KEY_IS_POSTPONED_TRANSITION = "KEY_IS_POSTPONED_TRANSITION"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val animation =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)

        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments?.getBoolean(KEY_IS_POSTPONED_TRANSITION) == true) {
            postponeEnterTransition()
        }

        with(binding) {
            tvApodExampleDetailsDate.text = arguments?.getString(KEY_DATE)
            tvApodExampleDetailsTitle.text = arguments?.getString(KEY_APOD_TITLE)
            tvApodExampleDetailsExplanation.apply {
                setTextIsSelectable(true)
                text = arguments?.getString(KEY_APOD_EXPLANATION)
            }

            ivApodExampleDetails.load(arguments?.getString(KEY_APOD_URL)) {
                listener(onSuccess = { _, _ ->
                    if (arguments?.getBoolean(KEY_IS_POSTPONED_TRANSITION) == true) {
                        startPostponedEnterTransition()
                        showToast(requireContext(), "Postponed transition")
                    }
                }, onError = { _, _: Throwable ->
                    if (arguments?.getBoolean(KEY_IS_POSTPONED_TRANSITION) == true) {
                        startPostponedEnterTransition()
                    }
                })
            }
        }
    }
}