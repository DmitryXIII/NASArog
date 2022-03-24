package com.ineedyourcode.nasarog.model.dto.marsphotodto


import com.google.gson.annotations.SerializedName

data class MarsDto(
    val photos: List<MarsPhotoDto>
) {
    data class MarsPhotoDto(
        @SerializedName("earth_date")
        val earthDate: String,
        @SerializedName("img_src")
        val imgSrc: String
    )
}