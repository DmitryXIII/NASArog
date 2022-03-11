package com.ineedyourcode.nasarog.remoterepo.dto.earthphotodto


import com.google.gson.annotations.SerializedName

data class EarthPhotoItem(
    @SerializedName("centroid_coordinates")
    val centroidCoordinates: CentroidCoordinates,
    @SerializedName("date")
    val date: String,
    @SerializedName("image")
    val image: String
)