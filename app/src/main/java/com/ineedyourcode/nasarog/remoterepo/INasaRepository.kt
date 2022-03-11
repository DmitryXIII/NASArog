package com.ineedyourcode.nasarog.remoterepo

import com.ineedyourcode.nasarog.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface INasaRepository {
    @GET( "planetary/apod")
    fun getPictureOfTheDay(
        @Query("api_key") apiKey: String = BuildConfig.NASA_API_KEY,
        ): Call<PictureOfTheDayDto>

    @GET( "planetary/apod")
    fun getPictureOfTheDayFromDate(
        @Query("date") date: String,
        @Query("api_key") apiKey: String = BuildConfig.NASA_API_KEY,
    ): Call<PictureOfTheDayDto>
}