package com.ineedyourcode.nasarog.view.ui.features.recyclerview

import com.ineedyourcode.nasarog.model.dto.asteroidsdto.AsteroidListDto

interface OnAsteroidItemClickListener {
    fun onAsteroidItemClick(asteroid: AsteroidListDto.AsteroidDto)
}