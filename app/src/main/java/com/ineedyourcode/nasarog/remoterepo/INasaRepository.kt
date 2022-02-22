package com.ineedyourcode.nasarog.remoterepo

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface INasaRepository {
    @GET( "planetary/apod")
    fun getPictureOfTheDay(
        @Query("api_key") apiKey: String,

        ): Call<PictureOfTheDayDto>
}