package com.ineedyourcode.nasarog.remoterepo

import com.ineedyourcode.nasarog.BuildConfig
import com.ineedyourcode.nasarog.remoterepo.dto.PictureOfTheDayDto
import com.ineedyourcode.nasarog.remoterepo.dto.earthphotodto.EarthPhotoDateDto
import com.ineedyourcode.nasarog.remoterepo.dto.earthphotodto.EarthPhotoItem
import com.ineedyourcode.nasarog.remoterepo.dto.marsphotodto.MarsDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface INasaApi {
    @GET("planetary/apod")
    fun getPictureOfTheDay(@Query("date") date: String): Call<PictureOfTheDayDto>

    @GET("EPIC/api/natural/date/{date}")
    fun getEarthPhoto(@Path("date") date: String): Call<List<EarthPhotoItem>>

    @GET("EPIC/api/natural/all")
    fun getEarthPhotoDates(): Call<List<EarthPhotoDateDto>>

    @GET("mars-photos/api/v1/rovers/curiosity/photos")
    fun getMarsPhoto(@Query("earth_date") earthDate: String): Call<MarsDto>




//    @GET("planetary/apod")
//    fun getPictureOfTheDay(
//        @Query("date") date: String,
//        @Query("api_key") apiKey: String = BuildConfig.NASA_API_KEY
//    ): Call<PictureOfTheDayDto>
//
//    @GET("EPIC/api/natural/date/{date}")
//    fun getEarthPhoto(@Path("date") date: String,
//                      @Query("api_key") apiKey: String = BuildConfig.NASA_API_KEY): Call<List<EarthPhotoItem>>
//
//    @GET("EPIC/api/natural/all")
//    fun getEarthPhotoDates(
//        @Query("api_key") apiKey: String = BuildConfig.NASA_API_KEY): Call<List<EarthPhotoDateDto>>
//
//    @GET("mars-photos/api/v1/rovers/curiosity/photos")
//    fun getMarsPhoto(@Query("earth_date") earthDate: String,
//                     @Query("api_key") apiKey: String = BuildConfig.NASA_API_KEY): Call<MarsDto>
}