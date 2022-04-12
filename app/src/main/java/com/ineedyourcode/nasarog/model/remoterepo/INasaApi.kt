package com.ineedyourcode.nasarog.model.remoterepo

import com.ineedyourcode.nasarog.model.dto.apoddto.PictureOfTheDayDto
import com.ineedyourcode.nasarog.model.dto.asteroidsdto.AsteroidListDto
import com.ineedyourcode.nasarog.model.dto.earthphotodto.EarthPhotoDateDto
import com.ineedyourcode.nasarog.model.dto.earthphotodto.EarthPhotoDto
import com.ineedyourcode.nasarog.model.dto.marsphotodto.MarsDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface INasaApi {
    @GET("planetary/apod")
    fun getPictureOfTheDay(@Query("date") date: String): Call<PictureOfTheDayDto>

    @GET("EPIC/api/natural/date/{date}")
    fun getEarthPhoto(@Path("date") date: String): Call<List<EarthPhotoDto>>

    @GET("EPIC/api/natural/all")
    fun getEarthPhotoDates(): Call<List<EarthPhotoDateDto>>

    @GET("mars-photos/api/v1/rovers/curiosity/photos")
    fun getMarsPhoto(@Query("earth_date") earthDate: String): Call<MarsDto>

    @GET("neo/rest/v1/feed")
    fun getAsteroidsData(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): Call<AsteroidListDto>
}