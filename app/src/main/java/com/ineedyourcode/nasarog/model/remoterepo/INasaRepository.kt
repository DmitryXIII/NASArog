package com.ineedyourcode.nasarog.model.remoterepo

import com.ineedyourcode.nasarog.model.dto.apoddto.PictureOfTheDayDto
import com.ineedyourcode.nasarog.model.dto.earthphotodto.EarthPhotoDateDto
import com.ineedyourcode.nasarog.model.dto.earthphotodto.EarthPhotoDto
import com.ineedyourcode.nasarog.model.dto.marsphotodto.MarsDto
import retrofit2.Callback

interface INasaRepository {
    fun getPictureOfTheDay(date: String, callback: Callback<PictureOfTheDayDto>)
    fun getEarthPhoto(date: String, callback: Callback<List<EarthPhotoDto>>)
    fun getEarthPhotoDates(callback: Callback<List<EarthPhotoDateDto>>)
    fun getMarsPhoto(earthDate: String, callback: Callback<MarsDto>)
}