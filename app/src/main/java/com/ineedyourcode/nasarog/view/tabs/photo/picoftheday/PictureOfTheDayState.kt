package com.ineedyourcode.nasarog.view.tabs.photo.picoftheday

import com.ineedyourcode.nasarog.remoterepo.dto.PictureOfTheDayDto

sealed class PictureOfTheDayState {
    data class Loading(val progress: Int?) : PictureOfTheDayState()
    data class Success(val pictureOfTheDay: PictureOfTheDayDto) : PictureOfTheDayState()
    data class Error(val error: Throwable) : PictureOfTheDayState()
}