package com.ineedyourcode.nasarog.model.dto.asteroidsdto

import com.google.gson.annotations.SerializedName

data class AsteroidDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("absolute_magnitude_h")
    val absoluteMagnitudeH: Float,
    @SerializedName("is_potentially_hazardous_asteroid")
    val isPotentiallyHazardousAsteroid: Boolean,
    @SerializedName("close_approach_date")
    val closeApproachDate: String
)