package com.ineedyourcode.nasarog.view.sharedelementtransition.badlivedata
//
//import android.os.Bundle
//import android.transition.TransitionInflater
//import android.view.View
//import androidx.navigation.fragment.findNavController
//import coil.load
//import com.ineedyourcode.nasarog.R
//import com.ineedyourcode.nasarog.databinding.FragmentSharedElementTransitionDetailsBinding
//import com.ineedyourcode.nasarog.utils.showToast
//import com.ineedyourcode.nasarog.view.basefragment.BaseFragment
//
//class NotStableAnimationDetailsFragment :
//    BaseFragment<FragmentSharedElementTransitionDetailsBinding>(FragmentSharedElementTransitionDetailsBinding::inflate) {
//
//    companion object {
//        const val KEY_ARGUMENTS_MAP = "KEY_MAP_VALUE"
//        const val KEY_DATE_TYPE = "KEY_DATE_TYPE"
//        const val KEY_IS_POSTPONED_TRANSITION = "KEY_IS_POSTPONED_TRANSITION"
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        val animation =
//            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
//
//        sharedElementEnterTransition = animation
//        sharedElementReturnTransition = animation
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val mapOfArguments = arguments?.getSerializable(KEY_ARGUMENTS_MAP) as HashMap<*, *>
//
//        if (mapOfArguments[KEY_IS_POSTPONED_TRANSITION] == true) {
//            postponeEnterTransition()
//        }
//
//        with(binding) {
//            when (mapOfArguments[KEY_DATE_TYPE]) {
//                NotStableAnimationFragment.DATE_TYPE_TODAY -> {
//                    tvApodExampleDetailsDate.text =
//                        mapOfArguments[NotStableAnimationFragment.KEY_TODAY_DATE].toString()
//                    tvApodExampleDetailsTitle.text =
//                        mapOfArguments[NotStableAnimationFragment.KEY_TODAY_TITLE].toString()
//                    tvApodExampleDetailsExplanation.apply {
//                        setTextIsSelectable(true)
//                        text = mapOfArguments[NotStableAnimationFragment.KEY_TODAY_EXPLANATION].toString()
//                    }
//
//                    ivApodExampleDetails.load(mapOfArguments[NotStableAnimationFragment.KEY_TODAY_HDURL].toString())
//                }
//
//                NotStableAnimationFragment.DATE_TYPE_YESTERDAY -> {
//                    tvApodExampleDetailsDate.text =
//                        mapOfArguments[NotStableAnimationFragment.KEY_YESTERDAY_DATE].toString()
//                    tvApodExampleDetailsTitle.text =
//                        mapOfArguments[NotStableAnimationFragment.KEY_YESTERDAY_TITLE].toString()
//                    tvApodExampleDetailsExplanation.apply {
//                        setTextIsSelectable(true)
//                        text =
//                            mapOfArguments[NotStableAnimationFragment.KEY_YESTERDAY_EXPLANATION].toString()
//                    }
//
//                    ivApodExampleDetails.load(mapOfArguments[NotStableAnimationFragment.KEY_YESTERDAY_HDURL].toString())
//                }
//
//                NotStableAnimationFragment.DATE_TYPE_BEFORE_YESTERDAY -> {
//                    tvApodExampleDetailsDate.text =
//                        mapOfArguments[NotStableAnimationFragment.KEY_BEFORE_YESTERDAY_DATE].toString()
//                    tvApodExampleDetailsTitle.text =
//                        mapOfArguments[NotStableAnimationFragment.KEY_BEFORE_YESTERDAY_TITLE].toString()
//                    tvApodExampleDetailsExplanation.apply {
//                        setTextIsSelectable(true)
//                        text =
//                            mapOfArguments[NotStableAnimationFragment.KEY_BEFORE_YESTERDAY_EXPLANATION].toString()
//                    }
//
//                    if (mapOfArguments[KEY_IS_POSTPONED_TRANSITION] == true) {
//                        ivApodExampleDetails.load(mapOfArguments[NotStableAnimationFragment.KEY_BEFORE_YESTERDAY_HDURL].toString()) {
//                            listener(onSuccess = { _, _ ->
//                                startPostponedEnterTransition()
//                                showToast(
//                                    requireContext(),
//                                    getString(R.string.postponed_transition)
//                                )
//                            }, onError = { _, _: Throwable ->
//                                startPostponedEnterTransition()
//                            })
//                        }
//                    }
//                }
//            }
//
//            findNavController().previousBackStackEntry?.savedStateHandle?.set(
//                NotStableAnimationFragment.KEY_BACK_STACK_ENTRY_MAP,
//                mapOfArguments
//            )
//        }
//    }
//}