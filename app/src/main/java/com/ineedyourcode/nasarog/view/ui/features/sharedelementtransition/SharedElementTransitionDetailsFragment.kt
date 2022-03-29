package com.ineedyourcode.nasarog.view.ui.features.sharedelementtransition

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import coil.load
import com.ineedyourcode.nasarog.R
import com.ineedyourcode.nasarog.databinding.FragmentFeaturesSharedElementTransitionDetailsBinding
import com.ineedyourcode.nasarog.utils.showToast
import com.ineedyourcode.nasarog.view.basefragment.BaseFragment

private const val KEY_ARG_IMAGE_URL = "KEY_IMAGE_URL"
private const val KEY_ARG_DATE = "KEY_ARG_DATE"
private const val KEY_ARG_TITLE = "KEY_ARG_TITLE"
private const val KEY_ARG_EXPLANATION = "KEY_ARG_EXPLANATION"

class SharedElementTransitionDetailsFragment :
    BaseFragment<FragmentFeaturesSharedElementTransitionDetailsBinding>(
        FragmentFeaturesSharedElementTransitionDetailsBinding::inflate
    ) {

    companion object {
        fun newInstance(date: String, title: String, explanation: String, imageUrl: String) =
            SharedElementTransitionDetailsFragment().apply {
                arguments = bundleOf(
                    KEY_ARG_IMAGE_URL to imageUrl,
                    KEY_ARG_DATE to date,
                    KEY_ARG_TITLE to title,
                    KEY_ARG_EXPLANATION to explanation
                )
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val animation =
//            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
//
//        sharedElementEnterTransition = animation
//        sharedElementReturnTransition = animation
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()

        with(binding) {

            tvApodExampleDetailsDate.text = arguments?.getString(KEY_ARG_DATE)
            tvApodExampleDetailsTitle.text = arguments?.getString(KEY_ARG_TITLE)
            tvApodExampleDetailsExplanation.apply {
                setTextIsSelectable(true)
                text = arguments?.getString(KEY_ARG_EXPLANATION)
            }

            ivApodExampleDetails.load(arguments?.getString(KEY_ARG_IMAGE_URL)) {
                listener(onSuccess = { _, _ ->
                    startPostponedEnterTransition()
                    showToast(
                        requireContext(),
                        getString(R.string.postponed_transition)
                    )
                }, onError = { _, _: Throwable ->
                    startPostponedEnterTransition()
                })
            }
        }
    }
}