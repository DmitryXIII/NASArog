package com.ineedyourcode.nasarog.remoterepo.dto.marsphotodto


import com.google.gson.annotations.SerializedName

data class Photo(
    @SerializedName("earth_date")
    val earthDate: String,
    @SerializedName("img_src")
    val imgSrc: String
)