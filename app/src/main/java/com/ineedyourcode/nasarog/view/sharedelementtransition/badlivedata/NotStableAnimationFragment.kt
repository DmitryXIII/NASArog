package com.ineedyourcode.nasarog.view.sharedelementtransition.badlivedata
//
//import android.os.Bundle
//import android.view.View
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.core.os.bundleOf
//import androidx.fragment.app.viewModels
//import androidx.navigation.fragment.FragmentNavigatorExtras
//import androidx.navigation.fragment.findNavController
//import com.ineedyourcode.nasarog.R
//import com.ineedyourcode.nasarog.databinding.FragmentSharedElementTransitionBinding
//import com.ineedyourcode.nasarog.utils.*
//import com.ineedyourcode.nasarog.view.basefragment.BaseFragment
//import com.ineedyourcode.nasarog.view.sharedelementtransition.stableanimation.SharedElementTransitionState
//
///**
// * Фрагмент для демонстрации поведения observer, при котором приходят не все данные из liveData
// */
//
//class NotStableAnimationFragment :
//    BaseFragment<FragmentSharedElementTransitionBinding>(FragmentSharedElementTransitionBinding::inflate) {
//
//    private val apodExampleViewModel by viewModels<NotStableAnimationViewModel>()
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        apodExampleViewModel.getLiveData().observe(viewLifecycleOwner) {
//            renderDataToday(it)
//        }
//
//        apodExampleViewModel.getApod()
//    }
//
//    private fun renderDataToday(state: SharedElementTransitionState) = with(binding) {
//        when (state) {
//            SharedElementTransitionState.TodayLoading -> {
//                setVisibilityOnStateLoading(
//                    progressBarToday,
//                    cardApodExampleToday
//                )
//            }
//
//            SharedElementTransitionState.YesterdayLoading -> {
//                setVisibilityOnStateLoading(
//                    progressBarYesterday,
//                    cardApodExampleYesterday
//                )
//            }
//
//            SharedElementTransitionState.BeforeYesterdayLoading -> {
//                setVisibilityOnStateLoading(
//                    progressBarBeforeYesterday,
//                    cardApodExampleBeforeYesterday
//                )
//            }
//
//            is SharedElementTransitionState.TodaySuccess -> {
//                tvApodExampleTodayDate.text = getCurrentDate()
//                tvApodExampleTodayTitle.text = state.apodToday.title
//                ivApodExampleToday.loadWithTransformAndCallback(state.apodToday.hdurl, 200) {
//                    setVisibilityOnStateSuccess(progressBarToday, cardApodExampleToday)
//                    setOnImageClickNavigation(
//                        ivApodExampleToday,
//                        tvApodExampleTodayDate,
//                        tvApodExampleTodayTitle,
//                        DATE_TYPE_TODAY
//                    )
//                }
//
//                addValuesToMapOfArguments(
//                    DATE_TYPE_TODAY,
//                    state.apodToday.hdurl,
//                    state.apodToday.explanation
//                )
//            }
//
//            is SharedElementTransitionState.YesterdaySuccess -> {
//                tvApodExampleYesterdayDate.text = getYesterdayDate()
//                tvApodExampleYesterdayTitle.text = state.apodYesterday.title
//                ivApodExampleYesterday.loadWithTransformAndCallback(
//                    state.apodYesterday.hdurl,
//                    200
//                ) {
//                    setOnImageClickNavigation(
//                        ivApodExampleYesterday,
//                        tvApodExampleYesterdayDate,
//                        tvApodExampleYesterdayTitle,
//                        DATE_TYPE_YESTERDAY
//                    )
//                    setVisibilityOnStateSuccess(
//                        progressBarYesterday,
//                        cardApodExampleYesterday
//                    )
//                }
//
//                addValuesToMapOfArguments(
//                    DATE_TYPE_YESTERDAY,
//                    state.apodYesterday.hdurl,
//                    state.apodYesterday.explanation
//                )
//            }
//
//            is SharedElementTransitionState.BeforeYesterdaySuccess -> {
//                tvApodExampleBeforeYesterdayDate.text = getBeforeYesterdayDate()
//                tvApodExampleBeforeYesterdayTitle.text = state.apodBeforeYesterday.title
//                ivApodExampleBeforeYesterday.loadWithTransformAndCallback(
//                    state.apodBeforeYesterday.hdurl,
//                    200
//                ) {
//                    setOnImageClickNavigation(
//                        ivApodExampleBeforeYesterday,
//                        tvApodExampleBeforeYesterdayDate,
//                        tvApodExampleBeforeYesterdayTitle,
//                        DATE_TYPE_BEFORE_YESTERDAY,
//                        postponed = true
//                    )
//                    setVisibilityOnStateSuccess(
//                        progressBarBeforeYesterday,
//                        cardApodExampleBeforeYesterday
//                    )
//
//                    addValuesToMapOfArguments(
//                        DATE_TYPE_BEFORE_YESTERDAY,
//                        state.apodBeforeYesterday.hdurl,
//                        state.apodBeforeYesterday.explanation
//                    )
//                }
//            }
//
//            is SharedElementTransitionState.TodayError -> {
//                view?.showSnackWithoutAction(state.error.message.toString())
//            }
//
//            is SharedElementTransitionState.YesterdayError -> {
//                view?.showSnackWithoutAction(state.error.message.toString())
//            }
//
//            is SharedElementTransitionState.BeforeYesterdayError -> {
//                view?.showSnackWithoutAction(state.error.message.toString())
//            }
//        }
//    }
//
//    private fun setOnImageClickNavigation(
//        image: ImageView,
//        date: TextView,
//        title: TextView,
//        dateType: String,
//        postponed: Boolean = false,
//        explanationView: TextView = binding.tvEmptyExplanation
//    ) {
//
//        image.setOnClickListener {
//            mapOfArguments[NotStableAnimationDetailsFragment.KEY_DATE_TYPE] = dateType
//            mapOfArguments[NotStableAnimationDetailsFragment.KEY_IS_POSTPONED_TRANSITION] =
//                postponed
//
//            val extras = FragmentNavigatorExtras(
//                image to getString(R.string.transition_apod_details),
//                date to getString(R.string.transition_details_date),
//                title to getString(R.string.transition_details_title),
//                explanationView to getString(R.string.transition_details_explanation)
//            )
//
//            findNavController().navigate(
//                R.id.action_apodExampleFragment_to_apodExampleDetailsFragment,
//                bundleOf(NotStableAnimationDetailsFragment.KEY_ARGUMENTS_MAP to mapOfArguments),
//                null,
//                extras
//            )
//        }
//    }
//
//    private fun addValuesToMapOfArguments(dateType: String, hdurl: String, explanation: String) =
//        with(binding) {
//            when (dateType) {
//                DATE_TYPE_TODAY -> {
//                    mapOfArguments[KEY_TODAY_DATE] = tvApodExampleTodayDate.text
//                    mapOfArguments[KEY_TODAY_TITLE] = tvApodExampleTodayTitle.text
//                    mapOfArguments[KEY_TODAY_HDURL] = hdurl
//                    mapOfArguments[KEY_TODAY_EXPLANATION] = explanation
//                }
//
//                DATE_TYPE_YESTERDAY -> {
//                    mapOfArguments[KEY_YESTERDAY_DATE] = tvApodExampleYesterdayDate.text
//                    mapOfArguments[KEY_YESTERDAY_TITLE] = tvApodExampleYesterdayTitle.text
//                    mapOfArguments[KEY_YESTERDAY_HDURL] = hdurl
//                    mapOfArguments[KEY_YESTERDAY_EXPLANATION] = explanation
//                }
//
//                DATE_TYPE_BEFORE_YESTERDAY -> {
//                    mapOfArguments[KEY_BEFORE_YESTERDAY_DATE] =
//                        tvApodExampleBeforeYesterdayDate.text
//                    mapOfArguments[KEY_BEFORE_YESTERDAY_TITLE] =
//                        tvApodExampleBeforeYesterdayTitle.text
//                    mapOfArguments[KEY_BEFORE_YESTERDAY_HDURL] = hdurl
//                    mapOfArguments[KEY_BEFORE_YESTERDAY_EXPLANATION] = explanation
//                }
//            }
//        }
//}