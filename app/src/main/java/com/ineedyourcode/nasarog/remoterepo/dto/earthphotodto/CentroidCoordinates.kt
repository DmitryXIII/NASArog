package com.ineedyourcode.nasarog.remoterepo.dto.earthphotodto


import com.google.gson.annotations.SerializedName

data class CentroidCoordinates(
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lon")
    val lon: Double
)