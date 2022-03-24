package com.ineedyourcode.nasarog.view.ui.tabspager.tabs.apod

import com.ineedyourcode.nasarog.model.dto.apoddto.PictureOfTheDayDto

sealed class TabApodState {
    object Loading : TabApodState()
    data class Success(val pictureOfTheDay: PictureOfTheDayDto) : TabApodState()
    data class Error(val error: Throwable) : TabApodState()
}