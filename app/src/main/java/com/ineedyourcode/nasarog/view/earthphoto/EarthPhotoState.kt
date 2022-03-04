package com.ineedyourcode.nasarog.view.earthphoto

import com.ineedyourcode.nasarog.remoterepo.dto.PictureOfTheDayDto
import com.ineedyourcode.nasarog.remoterepo.dto.earthphotodto.EarthPhotoItem

sealed class EarthPhotoState {
    data class Loading(val progress: Int?) : EarthPhotoState()
    data class Success(val earthPhoto: EarthPhotoItem) : EarthPhotoState()
    data class Error(val error: Throwable) : EarthPhotoState()
}

