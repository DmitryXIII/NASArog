package com.ineedyourcode.nasarog.remoterepo

import com.ineedyourcode.nasarog.BuildConfig
import com.ineedyourcode.nasarog.remoterepo.dto.PictureOfTheDayDto
import com.ineedyourcode.nasarog.remoterepo.dto.earthphotodto.EarthPhotoDateDto
import com.ineedyourcode.nasarog.remoterepo.dto.earthphotodto.EarthPhotoItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface INasaRepository {
    @GET("planetary/apod")
    fun getPictureOfTheDay(
        @Query("date") date: String,
        @Query("api_key") apiKey: String = BuildConfig.NASA_API_KEY,
    ): Call<PictureOfTheDayDto>

    @GET("EPIC/api/natural/date/{date}")
    fun getEarthPhoto(
        @Path ("date") date: String,
        @Query("api_key") apiKey: String = BuildConfig.NASA_API_KEY,
    ): Call<List<EarthPhotoItem>>

    @GET("EPIC/api/natural/all")
    fun getEarthPhotoDates(
        @Query("api_key") apiKey: String = BuildConfig.NASA_API_KEY,
    ): Call<List<EarthPhotoDateDto>>
}