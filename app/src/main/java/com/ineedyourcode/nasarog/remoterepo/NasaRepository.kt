package com.ineedyourcode.nasarog.remoterepo

import com.google.gson.GsonBuilder
import com.ineedyourcode.nasarog.remoterepo.dto.PictureOfTheDayDto
import com.ineedyourcode.nasarog.remoterepo.dto.earthphotodto.EarthPhotoDateDto
import com.ineedyourcode.nasarog.remoterepo.dto.earthphotodto.EarthPhotoItem
import com.ineedyourcode.nasarog.remoterepo.dto.marsphotodto.MarsDto
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.nasa.gov/"

class NasaRepository : INasaRepository {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .client(createClient())
            .build().create(INasaApi::class.java)
    }

    override fun getPictureOfTheDay(date: String, callback: Callback<PictureOfTheDayDto>) {
        retrofit.getPictureOfTheDay(date).enqueue(callback)
    }

    override fun getEarthPhoto(date: String, callback: Callback<List<EarthPhotoItem>>) {
        retrofit.getEarthPhoto(date).enqueue(callback)
    }

    override fun getEarthPhotoDates(callback: Callback<List<EarthPhotoDateDto>>) {
        retrofit.getEarthPhotoDates().enqueue(callback)
    }

    override fun getMarsPhoto(earthDate: String, callback: Callback<MarsDto>) {
        retrofit.getMarsPhoto(earthDate).enqueue(callback)
    }

    private fun createClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(NasaApiKeyInterceptor())
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }
}