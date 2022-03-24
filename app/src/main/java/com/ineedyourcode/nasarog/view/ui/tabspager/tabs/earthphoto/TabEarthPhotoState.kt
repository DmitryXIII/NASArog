package com.ineedyourcode.nasarog.view.ui.tabspager.tabs.earthphoto

import com.ineedyourcode.nasarog.model.dto.earthphotodto.EarthPhotoDateDto
import com.ineedyourcode.nasarog.model.dto.earthphotodto.EarthPhotoDto

sealed class TabEarthPhotoState {
    object Loading: TabEarthPhotoState()
    data class DatesSuccess(val mListOfDates: List<EarthPhotoDateDto>) : TabEarthPhotoState()
    data class PhotoSuccess(val earthPhoto: EarthPhotoDto) : TabEarthPhotoState()
    data class Error(val error: Throwable) : TabEarthPhotoState()
}


