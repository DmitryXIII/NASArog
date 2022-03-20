package com.ineedyourcode.nasarog.view.sharedtransition

import com.ineedyourcode.nasarog.remoterepo.dto.PictureOfTheDayDto

sealed class ApodExampleState {
    object TodayLoading : ApodExampleState()
    object YesterdayLoading : ApodExampleState()
    object BeforeYesterdayLoading : ApodExampleState()
    data class TodaySuccess(val apodToday: PictureOfTheDayDto) : ApodExampleState()
    data class YesterdaySuccess(val apodYesterday: PictureOfTheDayDto) : ApodExampleState()
    data class BeforeYesterdaySuccess(val apodBeforeYesterday: PictureOfTheDayDto) : ApodExampleState()
    data class TodayError(val error: Throwable) : ApodExampleState()
    data class YesterdayError(val error: Throwable) : ApodExampleState()
    data class BeforeYesterdayError(val error: Throwable) : ApodExampleState()
}