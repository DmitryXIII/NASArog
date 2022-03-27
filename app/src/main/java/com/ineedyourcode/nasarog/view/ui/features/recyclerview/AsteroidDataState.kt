package com.ineedyourcode.nasarog.view.ui.features.recyclerview

import com.ineedyourcode.nasarog.model.dto.asteroidsdto.AsteroidListDto

sealed class AsteroidDataState {
    object Loading : AsteroidDataState()
    data class AsteroidDataSuccess(val asteroidList: List<AsteroidListDto.AsteroidDto>) : AsteroidDataState()
    data class Error(val error: Throwable) : AsteroidDataState()
}