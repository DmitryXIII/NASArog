package com.ineedyourcode.nasarog.view.sharedtransition

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import coil.load
import com.ineedyourcode.nasarog.R
import com.ineedyourcode.nasarog.databinding.FragmentApodExampleBinding
import com.ineedyourcode.nasarog.utils.*
import com.ineedyourcode.nasarog.view.basefragment.BaseFragment

class ApodExampleFragment :
    BaseFragment<FragmentApodExampleBinding>(FragmentApodExampleBinding::inflate) {

    private val mapOfArguments = HashMap<String, Any>()
    private val apodExampleViewModel by viewModels<ApodExampleViewModel>()

    companion object {
        const val DATE_TYPE_TODAY = "TODAY"
        const val DATE_TYPE_YESTERDAY = "YESTERDAY"
        const val DATE_TYPE_BEFORE_YESTERDAY = "BEFORE_YESTERDAY"
        const val KEY_BACK_STACK_ENTRY_MAP = "KEY_BACK_STACK_ENTRY_MAP"
        const val KEY_TODAY_DATE = "KEY_TODAY_DATE"
        const val KEY_TODAY_TITLE = "KEY_TODAY_TITLE"
        const val KEY_TODAY_HDURL = "KEY_TODAY_HDURL"
        const val KEY_TODAY_EXPLANATION = "KEY_TODAY_EXPLANATION"
        const val KEY_YESTERDAY_DATE = "KEY_YESTERDAY_DATE"
        const val KEY_YESTERDAY_TITLE = "KEY_YESTERDAY_TITLE"
        const val KEY_YESTERDAY_HDURL = "KEY_YESTERDAY_HDURL"
        const val KEY_YESTERDAY_EXPLANATION = "KEY_YESTERDAY_EXPLANATION"
        const val KEY_BEFORE_YESTERDAY_DATE = "KEY_BEFORE_YESTERDAY_DATE"
        const val KEY_BEFORE_YESTERDAY_TITLE = "KEY_BEFORE_YESTERDAY_TITLE"
        const val KEY_BEFORE_YESTERDAY_HDURL = "KEY_BEFORE_YESTERDAY_HDURL"
        const val KEY_BEFORE_YESTERDAY_EXPLANATION = "KEY_BEFORE_YESTERDAY_EXPLANATION"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backStateEntryData = findNavController()
            .currentBackStackEntry?.savedStateHandle?.getLiveData<Map<String, String>>(
                KEY_BACK_STACK_ENTRY_MAP
            )

        backStateEntryData?.observe(viewLifecycleOwner) { mapResult ->
            with(binding) {
                tvApodExampleTodayDate.text = mapResult[KEY_TODAY_DATE]
                tvApodExampleTodayTitle.text = mapResult[KEY_TODAY_TITLE]
                ivApodExampleToday.load(mapResult[KEY_TODAY_HDURL])

                tvApodExampleYesterdayDate.text = mapResult[KEY_YESTERDAY_DATE]
                tvApodExampleYesterdayTitle.text = mapResult[KEY_YESTERDAY_TITLE]
                ivApodExampleYesterday.load(mapResult[KEY_YESTERDAY_HDURL])

                tvApodExampleBeforeYesterdayDate.text =
                    mapResult[KEY_BEFORE_YESTERDAY_DATE]
                tvApodExampleBeforeYesterdayTitle.text =
                    mapResult[KEY_BEFORE_YESTERDAY_TITLE]
                ivApodExampleBeforeYesterday.load(mapResult[KEY_BEFORE_YESTERDAY_HDURL])

                setOnImageClickNavigation(
                    ivApodExampleToday,
                    tvApodExampleTodayDate,
                    tvApodExampleTodayTitle,
                    DATE_TYPE_TODAY
                )

                setOnImageClickNavigation(
                    ivApodExampleYesterday,
                    tvApodExampleYesterdayDate,
                    tvApodExampleYesterdayTitle,
                    DATE_TYPE_YESTERDAY
                )

                setOnImageClickNavigation(
                    ivApodExampleBeforeYesterday,
                    tvApodExampleBeforeYesterdayDate,
                    tvApodExampleBeforeYesterdayTitle,
                    DATE_TYPE_BEFORE_YESTERDAY,
                    postponed = true
                )
            }
        }

        if (savedInstanceState != null || backStateEntryData?.value == null) {

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
                ivApodExampleToday.loadWithTransformAndCallback(state.apodToday.hdurl, 200) {
                    setVisibilityOnStateSuccess(progressBarToday, cardApodExampleToday)
                    setOnImageClickNavigation(
                        ivApodExampleToday,
                        tvApodExampleTodayDate,
                        tvApodExampleTodayTitle,
                        DATE_TYPE_TODAY
                    )
                }

                addValuesToMapOfArguments(
                    DATE_TYPE_TODAY,
                    state.apodToday.hdurl,
                    state.apodToday.explanation
                )
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
                    200
                ) {
                    setOnImageClickNavigation(
                        ivApodExampleYesterday,
                        tvApodExampleYesterdayDate,
                        tvApodExampleYesterdayTitle,
                        DATE_TYPE_YESTERDAY
                    )
                    setVisibilityOnStateSuccess(
                        progressBarYesterday,
                        cardApodExampleYesterday
                    )
                }

                addValuesToMapOfArguments(
                    DATE_TYPE_YESTERDAY,
                    state.apodYesterday.hdurl,
                    state.apodYesterday.explanation
                )
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
                    200
                ) {
                    setOnImageClickNavigation(
                        ivApodExampleBeforeYesterday,
                        tvApodExampleBeforeYesterdayDate,
                        tvApodExampleBeforeYesterdayTitle,
                        DATE_TYPE_BEFORE_YESTERDAY,
                        postponed = true
                    )
                    setVisibilityOnStateSuccess(
                        progressBarBeforeYesterday,
                        cardApodExampleBeforeYesterday
                    )

                    addValuesToMapOfArguments(
                        DATE_TYPE_BEFORE_YESTERDAY,
                        state.apodBeforeYesterday.hdurl,
                        state.apodBeforeYesterday.explanation
                    )
                }
            }

            is ApodExampleState.YesterdayError -> {
                view?.showSnackWithoutAction(state.error.message.toString())
            }

            else -> {}
        }
    }

    private fun setOnImageClickNavigation(
        image: ImageView,
        date: TextView,
        title: TextView,
        dateType: String,
        postponed: Boolean = false,
        explanationView: TextView = binding.tvEmptyExplanation
    ) {

        image.setOnClickListener {
            mapOfArguments[ApodExampleDetailsFragment.KEY_DATE_TYPE] = dateType
            mapOfArguments[ApodExampleDetailsFragment.KEY_IS_POSTPONED_TRANSITION] = postponed

            val extras = FragmentNavigatorExtras(
                image to getString(R.string.transition_apod_details),
                date to getString(R.string.transition_details_date),
                title to getString(R.string.transition_details_title),
                explanationView to getString(R.string.transition_details_explanation)
            )

            findNavController().navigate(
                R.id.action_apodExampleFragment_to_apodExampleDetailsFragment,
                bundleOf(ApodExampleDetailsFragment.KEY_ARGUMENTS_MAP to mapOfArguments),
                null,
                extras
            )
        }
    }

    private fun addValuesToMapOfArguments(dateType: String, hdurl: String, explanation: String) =
        with(binding) {
            when (dateType) {
                DATE_TYPE_TODAY -> {
                    mapOfArguments[KEY_TODAY_DATE] = tvApodExampleTodayDate.text
                    mapOfArguments[KEY_TODAY_TITLE] = tvApodExampleTodayTitle.text
                    mapOfArguments[KEY_TODAY_HDURL] = hdurl
                    mapOfArguments[KEY_TODAY_EXPLANATION] = explanation
                }

                DATE_TYPE_YESTERDAY -> {
                    mapOfArguments[KEY_YESTERDAY_DATE] = tvApodExampleYesterdayDate.text
                    mapOfArguments[KEY_YESTERDAY_TITLE] = tvApodExampleYesterdayTitle.text
                    mapOfArguments[KEY_YESTERDAY_HDURL] = hdurl
                    mapOfArguments[KEY_YESTERDAY_EXPLANATION] = explanation
                }

                DATE_TYPE_BEFORE_YESTERDAY -> {
                    mapOfArguments[KEY_BEFORE_YESTERDAY_DATE] =
                        tvApodExampleBeforeYesterdayDate.text
                    mapOfArguments[KEY_BEFORE_YESTERDAY_TITLE] =
                        tvApodExampleBeforeYesterdayTitle.text
                    mapOfArguments[KEY_BEFORE_YESTERDAY_HDURL] = hdurl
                    mapOfArguments[KEY_BEFORE_YESTERDAY_EXPLANATION] = explanation
                }
            }
        }
}