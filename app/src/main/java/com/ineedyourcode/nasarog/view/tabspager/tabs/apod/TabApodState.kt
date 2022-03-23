package com.ineedyourcode.nasarog.view.tabspager.tabs.apod

import com.ineedyourcode.nasarog.remoterepo.dto.PictureOfTheDayDto

sealed class TabApodState {
    object Loading : TabApodState()
    data class Success(val pictureOfTheDay: PictureOfTheDayDto) : TabApodState()
    data class Error(val error: Throwable) : TabApodState()
}