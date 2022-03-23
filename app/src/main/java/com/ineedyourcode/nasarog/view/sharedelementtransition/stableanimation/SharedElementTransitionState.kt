package com.ineedyourcode.nasarog.view.sharedelementtransition.stableanimation

import com.ineedyourcode.nasarog.remoterepo.dto.PictureOfTheDayDto

sealed class SharedElementTransitionState {
    object TodayLoading : SharedElementTransitionState()
    object YesterdayLoading : SharedElementTransitionState()
    object BeforeYesterdayLoading : SharedElementTransitionState()
    data class TodaySuccess(val apodToday: PictureOfTheDayDto) : SharedElementTransitionState()
    data class YesterdaySuccess(val apodYesterday: PictureOfTheDayDto) : SharedElementTransitionState()
    data class BeforeYesterdaySuccess(val apodBeforeYesterday: PictureOfTheDayDto) : SharedElementTransitionState()
    data class TodayError(val error: Throwable) : SharedElementTransitionState()
    data class YesterdayError(val error: Throwable) : SharedElementTransitionState()
    data class BeforeYesterdayError(val error: Throwable) : SharedElementTransitionState()
}