package com.ineedyourcode.nasarog.view.tabs.photo.earthphoto

import com.ineedyourcode.nasarog.remoterepo.dto.earthphotodto.EarthPhotoDateDto
import com.ineedyourcode.nasarog.remoterepo.dto.earthphotodto.EarthPhotoItem

sealed class EarthPhotoState {
    object Loading: EarthPhotoState()
    data class DatesSuccess(val mListOfDates: List<EarthPhotoDateDto>) : EarthPhotoState()
    data class PhotoSuccess(val earthPhoto: EarthPhotoItem) : EarthPhotoState()
    data class Error(val error: Throwable) : EarthPhotoState()
}

