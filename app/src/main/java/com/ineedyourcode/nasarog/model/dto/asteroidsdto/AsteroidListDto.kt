package com.ineedyourcode.nasarog.model.dto.asteroidsdto

import com.google.gson.annotations.SerializedName

data class AsteroidListDto(
    @SerializedName("element_count")
    val elementCount: Int,
    @SerializedName("near_earth_objects")
    val nearEarthObjects: Map<String, List<AsteroidDto>>
) {
    data class AsteroidDto(
        @SerializedName("id")
        val id: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("absolute_magnitude_h")
        val absoluteMagnitudeH: Float,
        @SerializedName("is_potentially_hazardous_asteroid")
        val isPotentiallyHazardousAsteroid: Boolean,
        @SerializedName("close_approach_data")
        val closeApproachData: List<CloseApproachData>,
        var type: Int
    ) {
        data class CloseApproachData(
            @SerializedName("close_approach_date")
            val closeApproachDate: String
        )
    }
}