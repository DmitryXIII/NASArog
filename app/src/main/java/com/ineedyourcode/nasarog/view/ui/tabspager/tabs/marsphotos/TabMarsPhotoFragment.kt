package com.ineedyourcode.nasarog.view.ui.tabspager.tabs.marsphotos

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.viewModels
import androidx.transition.ChangeBounds
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.ineedyourcode.nasarog.R
import com.ineedyourcode.nasarog.databinding.FragmentTabMarsPhotoBinding
import com.ineedyourcode.nasarog.model.dto.marsphotodto.MarsDto
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
private const val ANIMATION_DURATION = 1000L
private const val INTERPOLATOR_TENSION = 2f

class TabMarsPhotoFragment :
    BaseFragment<FragmentTabMarsPhotoBinding>(FragmentTabMarsPhotoBinding::inflate) {

    private var isFirstOnResume = true

    // request вынесен из onViewCreated в onResume, т.к. в TabLayout у фрагмента onAttach срабатывает уже при
    // открытой соседней вкладке. Соответственно, вкладка с этим фрагментом еще может быть не активна,
    // но уже сработал onViewCreated, в котором запускается стартовая анимация, которую пользователь не видит.
    // Плюс некорректно отрабатывает TransitionListener.
    // Поэтому request отправляется при первом onResume, т.е. когда пользователь гарантированно открыл вкладку в TabLayout
    override fun onResume() {
        if (isFirstOnResume) {
            marsPhotoViewModel.getMarsPhotoRequest()
        }

        isFirstOnResume = false
        super.onResume()
    }

    // в ответе сервера приходит массив фотографий, этот индекс используется для пролистывания фотографий в фрагменте
    private var loadedImageIndex = ZERO_LOADED_IMAGE_INDEX

    private val marsPhotoViewModel by viewModels<TabMarsPhotoViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        marsPhotoViewModel.getLiveData().observe(viewLifecycleOwner) {
            renderData(it)
        }
    }

    private fun renderData(stateTab: TabMarsPhotoState) = with(binding) {
        when (stateTab) {
            TabMarsPhotoState.Loading -> {
                setVisibilityOnStateLoading(marsPhotoSpinKit, groupMarsPhoto)
            }

            is TabMarsPhotoState.MarsPhotoSuccess -> {
                Log.d("MARSTAG", "LOADING SUCCESS")
                // стартовая анимация,
                // загрузка первой фотографии из массива
                animateMarsPhotoUi(
                    binding.rootContainer,
                    ivMarsPhoto,
                    stateTab.marsPhoto.photos[loadedImageIndex].imgSrc,
                    groupMarsPhoto,
                    marsPhotoSpinKit
                )

                tvDateMarsPhoto.text =
                    convertNasaDateFormatToMyFormat(stateTab.marsPhoto.photos[loadedImageIndex].earthDate)

                // отображение количества фотографий в формате x/y (x - текущая фотография, y - всего фотографий)
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
            }

            is TabMarsPhotoState.Error -> {
                Log.d(LOG_TAG, stateTab.error.message.toString())
            }
        }
    }

    private fun animateMarsPhotoUi(
        rootContainer: ConstraintLayout,
        marsPhotoContainer: ImageView,
        imageSrc: String,
        groupMarsPhoto: Group,
        marsPhotoSpinKit: ProgressBar
    ) {
        ConstraintSet().apply {
            clone(rootContainer)
            connect(
                R.id.iv_arrow_left,
                ConstraintSet.START,
                R.id.guideline_begin,
                ConstraintSet.START
            )
            connect(R.id.iv_arrow_right, ConstraintSet.END, R.id.guideline_end, ConstraintSet.END)
            clear(R.id.iv_arrow_left, ConstraintSet.END)
            clear(R.id.iv_arrow_right, ConstraintSet.START)
            applyTo(rootContainer)
        }

        TransitionManager.beginDelayedTransition(rootContainer, ChangeBounds().apply {
            interpolator = AnticipateOvershootInterpolator(INTERPOLATOR_TENSION)
            duration = ANIMATION_DURATION
            addListener(object : Transition.TransitionListener {
                override fun onTransitionStart(transition: Transition) {}

                override fun onTransitionEnd(transition: Transition) {
                    marsPhotoContainer.loadWithTransform(
                        imageSrc,
                        CROSSFADE_DURATION,
                        IMAGE_CORNER_RADIUS
                    )

                    setVisibilityOnStateSuccess(marsPhotoSpinKit, groupMarsPhoto)
                }

                override fun onTransitionCancel(transition: Transition) {}
                override fun onTransitionPause(transition: Transition) {}
                override fun onTransitionResume(transition: Transition) {}
            })
        })
    }

    private fun setArrowClickListener(photosList: List<MarsDto.MarsPhotoDto>, direction: String) {
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