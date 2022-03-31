package com.ineedyourcode.nasarog.view.ui.tabspager.tabs.marsphotos

import com.ineedyourcode.nasarog.model.dto.marsphotodto.MarsDto

sealed class TabMarsPhotoState {
    object Loading : TabMarsPhotoState()
    data class MarsPhotoSuccess(val marsPhoto: MarsDto) : TabMarsPhotoState()
    data class Error(val error: Throwable) : TabMarsPhotoState()
}