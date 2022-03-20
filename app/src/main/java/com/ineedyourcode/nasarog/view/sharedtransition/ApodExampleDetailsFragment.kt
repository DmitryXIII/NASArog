package com.ineedyourcode.nasarog.view.sharedtransition

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import coil.load
import com.ineedyourcode.nasarog.databinding.FragmentApodExampleDetailsBinding
import com.ineedyourcode.nasarog.view.basefragment.BaseFragment

class ApodExampleDetailsFragment :
    BaseFragment<FragmentApodExampleDetailsBinding>(FragmentApodExampleDetailsBinding::inflate) {

    companion object {
        const val KEY_DATE = "KEY_DATE"
        const val KEY_APOD_URL = "KEY_APOD_URL"
        const val KEY_APOD_TITLE = "KEY_APOD_TITLE"
        const val KEY_APOD_EXPLANATION = "KEY_APOD_EXPLANATION"
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

        with(binding) {
            tvApodExampleDetailsDate.text = arguments?.getString(KEY_DATE)
            tvApodExampleDetailsTitle.text = arguments?.getString(KEY_APOD_TITLE)
            tvApodExampleDetailsExplanation.apply {
                setTextIsSelectable(true)
                text = arguments?.getString(KEY_APOD_EXPLANATION)
            }

            arguments?.getString(KEY_APOD_URL)?.let {
                ivApodExampleDetails.load(it)
            }
        }
    }
}