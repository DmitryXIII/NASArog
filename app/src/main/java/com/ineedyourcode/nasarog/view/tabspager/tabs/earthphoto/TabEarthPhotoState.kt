package com.ineedyourcode.nasarog.view.tabspager.tabs.earthphoto

import com.ineedyourcode.nasarog.remoterepo.dto.earthphotodto.EarthPhotoDateDto
import com.ineedyourcode.nasarog.remoterepo.dto.earthphotodto.EarthPhotoItem

sealed class TabEarthPhotoState {
    object Loading: TabEarthPhotoState()
    data class DatesSuccess(val mListOfDates: List<EarthPhotoDateDto>) : TabEarthPhotoState()
    data class PhotoSuccess(val earthPhoto: EarthPhotoItem) : TabEarthPhotoState()
    data class Error(val error: Throwable) : TabEarthPhotoState()
}


