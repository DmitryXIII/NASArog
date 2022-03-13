package com.ineedyourcode.nasarog.remoterepo

import com.ineedyourcode.nasarog.remoterepo.dto.PictureOfTheDayDto
import com.ineedyourcode.nasarog.remoterepo.dto.earthphotodto.EarthPhotoDateDto
import com.ineedyourcode.nasarog.remoterepo.dto.earthphotodto.EarthPhotoItem
import com.ineedyourcode.nasarog.remoterepo.dto.marsphotodto.MarsDto
import retrofit2.Callback

interface INasaRepository {
    fun getPictureOfTheDay(date: String, callback: Callback<PictureOfTheDayDto>)
    fun getEarthPhoto(date: String, callback: Callback<List<EarthPhotoItem>>)
    fun getEarthPhotoDates(callback: Callback<List<EarthPhotoDateDto>>)
    fun getMarsPhoto(earthDate: String, callback: Callback<MarsDto>)
}