package com.ineedyourcode.nasarog.model.remoterepo

import com.google.gson.GsonBuilder
import com.ineedyourcode.nasarog.model.dto.apoddto.PictureOfTheDayDto
import com.ineedyourcode.nasarog.model.dto.asteroidsdto.AsteroidDto
import com.ineedyourcode.nasarog.model.dto.earthphotodto.EarthPhotoDateDto
import com.ineedyourcode.nasarog.model.dto.earthphotodto.EarthPhotoDto
import com.ineedyourcode.nasarog.model.dto.marsphotodto.MarsDto
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

    override fun getEarthPhoto(date: String, callback: Callback<List<EarthPhotoDto>>) {
        retrofit.getEarthPhoto(date).enqueue(callback)
    }

    override fun getEarthPhotoDates(callback: Callback<List<EarthPhotoDateDto>>) {
        retrofit.getEarthPhotoDates().enqueue(callback)
    }

    override fun getMarsPhoto(earthDate: String, callback: Callback<MarsDto>) {
        retrofit.getMarsPhoto(earthDate).enqueue(callback)
    }

    override fun getAsteroidsData(
        startDate: String,
        endDate: String,
        callback: Callback<List<AsteroidDto>>
    ) {
        retrofit.getAsteroidsData(startDate, endDate).enqueue(callback)
    }

    private fun createClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(NasaApiKeyInterceptor())
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }
}