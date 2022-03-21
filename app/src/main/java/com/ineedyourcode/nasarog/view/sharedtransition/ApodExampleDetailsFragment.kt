package com.ineedyourcode.nasarog.view.sharedtransition

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import coil.load
import com.ineedyourcode.nasarog.databinding.FragmentApodExampleDetailsBinding
import com.ineedyourcode.nasarog.utils.showToast
import com.ineedyourcode.nasarog.view.basefragment.BaseFragment

class ApodExampleDetailsFragment :
    BaseFragment<FragmentApodExampleDetailsBinding>(FragmentApodExampleDetailsBinding::inflate) {

    companion object {
        const val KEY_MAP_VALUE = "KEY_MAP_VALUE"
        const val KEY_DATE_TYPE = "KEY_DATE_TYPE"
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

        val valueMap = arguments?.getSerializable(KEY_MAP_VALUE) as HashMap<String, Any>

        if (arguments?.getBoolean(KEY_IS_POSTPONED_TRANSITION) == true) {
            postponeEnterTransition()
        }

        with(binding) {
            when (arguments?.getString(KEY_DATE_TYPE)) {
                ApodExampleFragment.DATE_TYPE_TODAY  -> {
                    tvApodExampleDetailsDate.text = valueMap[ApodExampleFragment.KEY_TODAY_DATE].toString()
                    tvApodExampleDetailsTitle.text = valueMap[ApodExampleFragment.KEY_TODAY_TITLE].toString()
                    tvApodExampleDetailsExplanation.apply {
                        setTextIsSelectable(true)
                        text = valueMap[ApodExampleFragment.KEY_TODAY_EXPLANATION].toString()
                    }

                    ivApodExampleDetails.load(valueMap[ApodExampleFragment.KEY_TODAY_HDURL].toString()) {
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

                ApodExampleFragment.DATE_TYPE_YESTERDAY -> {
                    tvApodExampleDetailsDate.text = valueMap[ApodExampleFragment.KEY_YESTERDAY_DATE].toString()
                    tvApodExampleDetailsTitle.text =
                        valueMap[ApodExampleFragment.KEY_YESTERDAY_TITLE].toString()
                    tvApodExampleDetailsExplanation.apply {
                        setTextIsSelectable(true)
                        text = valueMap[ApodExampleFragment.KEY_YESTERDAY_EXPLANATION].toString()
                    }

                    ivApodExampleDetails.load(valueMap[ApodExampleFragment.KEY_YESTERDAY_HDURL].toString()) {
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

                ApodExampleFragment.DATE_TYPE_BEFORE_YESTERDAY -> {
                    tvApodExampleDetailsDate.text =
                        valueMap[ApodExampleFragment.KEY_BEFORE_YESTERDAY_DATE].toString()
                    tvApodExampleDetailsTitle.text =
                        valueMap[ApodExampleFragment.KEY_BEFORE_YESTERDAY_TITLE].toString()
                    tvApodExampleDetailsExplanation.apply {
                        setTextIsSelectable(true)
                        text = valueMap[ApodExampleFragment.KEY_BEFORE_YESTERDAY_EXPLANATION].toString()
                    }

                    ivApodExampleDetails.load(valueMap[ApodExampleFragment.KEY_BEFORE_YESTERDAY_HDURL].toString()) {
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

            valueMap[ApodExampleFragment.KEY_BACK_STACK_STATE] = true

            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                ApodExampleFragment.KEY_BACK_STACK_STATE_VALUE_MAP,
                valueMap
            )
        }
    }
}