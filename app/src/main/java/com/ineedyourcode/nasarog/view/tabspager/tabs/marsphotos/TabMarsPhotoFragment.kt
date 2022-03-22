package com.ineedyourcode.nasarog.view.tabspager.tabs.marsphotos

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.ineedyourcode.nasarog.R
import com.ineedyourcode.nasarog.databinding.FragmentTabMarsPhotoBinding
import com.ineedyourcode.nasarog.remoterepo.dto.marsphotodto.MarsDto
import com.ineedyourcode.nasarog.utils.convertNasaDateFormatToMyFormat
import com.ineedyourcode.nasarog.utils.loadWithTransform
import com.ineedyourcode.nasarog.utils.setVisibilityOnStateLoading
import com.ineedyourcode.nasarog.utils.setVisibilityOnStateSuccess
import com.ineedyourcode.nasarog.view.basefragment.BaseFragment

private const val CROSSFADE_DURATION = 1000
private const val IMAGE_CORNER_RADIUS = 25f
private const val ARROW_BACK = "BACK"
private const val ARROW_FORWARD = "FORWARD"
private const val LOG_TAG = "MARS_PHOTO"
private const val ZERO_LOADED_IMAGE_INDEX = 0
private const val FIRST_LOADED_IMAGE_INDEX = 1

class TabMarsPhotoFragment :
    BaseFragment<FragmentTabMarsPhotoBinding>(FragmentTabMarsPhotoBinding::inflate) {

    // в ответе сервера приходит массив фотографий, этот индекс используется для пролистывания фотографий в фрагменте
    private var loadedImageIndex = ZERO_LOADED_IMAGE_INDEX

    private val marsPhotoViewModel by viewModels<TabMarsPhotoViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        marsPhotoViewModel.getLiveData().observe(viewLifecycleOwner) {
            renderData(it)
        }

        marsPhotoViewModel.getMarsPhotoRequest()
    }

    private fun renderData(stateTab: TabMarsPhotoState) = with(binding) {
        when (stateTab) {
            TabMarsPhotoState.Loading -> {
                setVisibilityOnStateLoading(marsPhotoSpinKit, groupMarsPhoto)
            }

            is TabMarsPhotoState.Error -> {
                Log.d(LOG_TAG, stateTab.error.message.toString())
            }

            is TabMarsPhotoState.MarsPhotoSuccess -> {
                ivMarsPhoto.loadWithTransform(
                    stateTab.marsPhoto.photos[loadedImageIndex].imgSrc,
                    CROSSFADE_DURATION,
                    IMAGE_CORNER_RADIUS
                )

                // загрузка первой фотографии из массива,
                // отображение количества фотографий в формате x/y (x - текущая фотография, y - всего фотографий)
                tvDateMarsPhoto.text =
                    convertNasaDateFormatToMyFormat(stateTab.marsPhoto.photos[loadedImageIndex].earthDate)
                tvNumberMarsPhoto.text =
                    getString(
                        R.string.mars_photo_number,
                        FIRST_LOADED_IMAGE_INDEX,
                        stateTab.marsPhoto.photos.size
                    )


                // пролистывание фотографий вперед
                ivArrowRight.setOnClickListener {
                    setArrowClickListener(
                        stateTab.marsPhoto.photos,
                        ARROW_FORWARD
                    )
                }

                // пролистывание фотографий назад
                ivArrowLeft.setOnClickListener {
                    setArrowClickListener(
                        stateTab.marsPhoto.photos,
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