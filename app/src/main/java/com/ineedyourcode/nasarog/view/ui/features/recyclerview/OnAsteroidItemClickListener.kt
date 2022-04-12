package com.ineedyourcode.nasarog.view.ui.features.recyclerview

import androidx.recyclerview.widget.RecyclerView
import com.ineedyourcode.nasarog.model.dto.asteroidsdto.AsteroidListDto

interface OnAsteroidItemClickListener {
    fun onAsteroidItemClick(asteroid: AsteroidListDto.AsteroidDto)
}

fun interface OnStartDragListener {
    fun onStartDrag(view: RecyclerView.ViewHolder)
}