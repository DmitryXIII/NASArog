package com.ineedyourcode.nasarog.view.ui.features.recyclerview

import com.ineedyourcode.nasarog.model.dto.asteroidsdto.AsteroidDto

sealed class AsteroidDataState {
    object Loading : AsteroidDataState()
    data class AsteroidDataSuccess(val asteroidsList: List<AsteroidDto>) : AsteroidDataState()
    data class Error(val error: Throwable) : AsteroidDataState()
}