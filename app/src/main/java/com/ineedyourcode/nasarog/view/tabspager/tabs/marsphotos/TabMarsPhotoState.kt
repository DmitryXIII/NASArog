package com.ineedyourcode.nasarog.view.tabspager.tabs.marsphotos

import com.ineedyourcode.nasarog.remoterepo.dto.marsphotodto.MarsDto

sealed class TabMarsPhotoState {
    object Loading : TabMarsPhotoState()
    data class MarsPhotoSuccess(val marsPhoto: MarsDto) : TabMarsPhotoState()
    data class Error(val error: Throwable) : TabMarsPhotoState()
}