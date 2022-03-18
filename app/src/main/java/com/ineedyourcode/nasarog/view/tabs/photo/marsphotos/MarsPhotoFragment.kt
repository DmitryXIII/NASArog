package com.ineedyourcode.nasarog.view.tabs.photo.marsphotos

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.ineedyourcode.nasarog.R
import com.ineedyourcode.nasarog.databinding.FragmentMarsPhotoBinding
import com.ineedyourcode.nasarog.remoterepo.dto.marsphotodto.MarsDto
import com.ineedyourcode.nasarog.utils.convertNasaDateFormatToMyFormat
import com.ineedyourcode.nasarog.utils.loadWithTransform
import com.ineedyourcode.nasarog.utils.setVisibilityOnStateLoading
import com.ineedyourcode.nasarog.utils.setVisibilityOnStateSuccess
import com.ineedyourcode.nasarog.view.BaseBindingFragment


private const val CROSSFADE_DURATION = 1000
private const val IMAGE_CORNER_RADIUS = 25f
private const val ARROW_BACK = "BACK"
private const val ARROW_FORWARD = "FORWARD"
private const val LOG_TAG = "MARS_PHOTO"
private const val ZERO_LOADED_IMAGE_INDEX = 0
private const val FIRST_LOADED_IMAGE_INDEX = 1

class MarsPhotoFragment :
    BaseBindingFragment<FragmentMarsPhotoBinding>(FragmentMarsPhotoBinding::inflate) {

    // в ответе сервера приходит массив фотографий, этот индекс используется для пролистывания фотографий в фрагменте
    private var loadedImageIndex = ZERO_LOADED_IMAGE_INDEX

    private val marsPhotoViewModel by viewModels<MarsPhotoViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        marsPhotoViewModel.getLiveData().observe(viewLifecycleOwner) {
            renderData(it)
        }

        marsPhotoViewModel.getMarsPhotoRequest()
    }

    private fun renderData(state: MarsPhotoState) = with(binding) {
        when (state) {
            MarsPhotoState.Loading -> {
                setVisibilityOnStateLoading(marsPhotoSpinKit, groupMarsPhoto)
            }

            is MarsPhotoState.Error -> {
                Log.d(LOG_TAG, state.error.message.toString())
            }

            is MarsPhotoState.MarsPhotoSuccess -> {
                ivMarsPhoto.loadWithTransform(
                    state.marsPhoto.photos[loadedImageIndex].imgSrc,
                    CROSSFADE_DURATION,
                    IMAGE_CORNER_RADIUS
                )

                // загрузка первой фотографии из массива,
                // отображение количества фотографий в формате x/y (x - текущая фотография, y - всего фотографий)
                tvDateMarsPhoto.text =
                    convertNasaDateFormatToMyFormat(state.marsPhoto.photos[loadedImageIndex].earthDate)
                tvNumberMarsPhoto.text =
                    getString(
                        R.string.mars_photo_number,
                        FIRST_LOADED_IMAGE_INDEX,
                        state.marsPhoto.photos.size
                    )


                // пролистывание фотографий вперед
                ivArrowRight.setOnClickListener {
                    setArrowClickListener(
                        state.marsPhoto.photos,
                        ARROW_FORWARD
                    )
                }

                // пролистывание фотографий назад
                ivArrowLeft.setOnClickListener {
                    setArrowClickListener(
                        state.marsPhoto.photos,
                        ARROW_BACK
                    )
                }
                setVisibilityOnStateSuccess(groupMarsPhoto, marsPhotoSpinKit)
            }
        }
    }

    private fun setArrowClickListener(photosList: List<MarsDto.Photo>, direction: String) {
        when (direction) {
            ARROW_BACK -> {
                if (loadedImageIndex == ZERO_LOADED_IMAGE_INDEX) {
                    loadedImageIndex = photosList.size - 1
                } else {
                    loadedImageIndex--
                }
            }
            ARROW_FORWARD -> {
                if (loadedImageIndex == photosList.size - 1) {
                    loadedImageIndex = ZERO_LOADED_IMAGE_INDEX
                } else {
                    loadedImageIndex++
                }
            }
        }

        binding.tvNumberMarsPhoto.text =
            getString(R.string.mars_photo_number, (loadedImageIndex + 1), photosList.size)
        binding.ivMarsPhoto.loadWithTransform(
            photosList[loadedImageIndex].imgSrc,
            CROSSFADE_DURATION,
            IMAGE_CORNER_RADIUS
        )
    }
}