package com.ineedyourcode.nasarog.view.tabs.photo.marsphotos

import com.ineedyourcode.nasarog.remoterepo.dto.marsphotodto.MarsDto

sealed class MarsPhotoState {
    object Loading : MarsPhotoState()
    data class MarsPhotoSuccess(val marsPhoto: MarsDto) : MarsPhotoState()
    data class Error(val error: Throwable) : MarsPhotoState()
}