package com.ineedyourcode.nasarog.model.dto.earthphotodto

import com.google.gson.annotations.SerializedName

data class EarthPhotoDto(
    @SerializedName("centroid_coordinates")
    val centroidCoordinates: CentroidCoordinatesDto,
    @SerializedName("date")
    val date: String,
    @SerializedName("image")
    val image: String
)