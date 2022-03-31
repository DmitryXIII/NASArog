package com.ineedyourcode.nasarog.model.dto.earthphotodto


import com.google.gson.annotations.SerializedName

data class CentroidCoordinatesDto(
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lon")
    val lon: Double
)