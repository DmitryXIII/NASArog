package com.ineedyourcode.nasarog.view.ui.features.recyclerview

import com.ineedyourcode.nasarog.model.dto.asteroidsdto.AsteroidListDto

sealed class AsteroidDataState {
    object Loading : AsteroidDataState()
    data class AsteroidDataSuccess(val asData: AsteroidListDto) : AsteroidDataState()
    data class Error(val error: Throwable) : AsteroidDataState()
}