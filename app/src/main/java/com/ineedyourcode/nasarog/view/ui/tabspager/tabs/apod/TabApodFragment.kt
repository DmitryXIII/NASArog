package com.ineedyourcode.nasarog.view.ui.tabspager.tabs.apod

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.OvershootInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.transition.ChangeBounds
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.ineedyourcode.nasarog.R
import com.ineedyourcode.nasarog.databinding.FragmentTabApodBinding
import com.ineedyourcode.nasarog.utils.*
import com.ineedyourcode.nasarog.view.basefragment.BaseFragment

private const val WIKI_URL = "https://ru.wikipedia.org/wiki/"
private const val CROSSFADE_DURATION = 500
private const val IMAGE_CORNER_RADIUS = 25f
private const val BOTTOMSHEET_PHOTO_DESCRIPTION_HEIGHT_COEFFICIENT = 0.75
private const val MEDIA_TYPE_VIDEO = "video"
private const val MEDIA_TYPE_IMAGE = "image"
private const val ARROWS_ROTATION_ANGLE = 180
private const val VIDEO_START_PLAYING_SECOND = 0f
private const val DEFAULT_ANIMATION_DURATION = 1000L
private const val APOD_COORDINATOR_ANIMATION_DURATION = 1000L
private const val DEFAULT_INTERPOLATOR_TENSION = 5f
private const val APOD_COORDINATOR_INTERPOLATOR_TENSION = 2f

class TabApodFragment :
    BaseFragment<FragmentTabApodBinding>(FragmentTabApodBinding::inflate) {

    private lateinit var apodBottomSheet: BottomSheetBehavior<ConstraintLayout>

    private var isAnimationRequired = true

    private val podViewModel by viewModels<TabApodViewModel>()

    companion object {
        fun newInstance() = TabApodFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycle.addObserver(binding.youTubePlayerView)

        setChips()

        podViewModel.getLiveData().observe(viewLifecycleOwner) {
            renderData(it)
        }

        apodBottomSheet = BottomSheetBehavior.from(binding.bottomSheetContainer)

        setBottomSheetCallback()

        // для тестирования воспроизведения видео - viewModel.getPictureOfTheDayRequest("2022-02-09")
        podViewModel.getPictureOfTheDayRequest()

        binding.tvDateOfPicture.text = convertNasaDateFormatToMyFormat(getCurrentDate())

        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent(Intent(Intent.ACTION_VIEW))).apply {
                data = Uri.parse("$WIKI_URL${binding.inputEditText.text}")
            })
        }
    }

    private fun setChips() = binding.chipGroup.setOnCheckedChangeListener { _, checkedId ->
        when (checkedId) {
            R.id.chip_before_yesterday -> {
                getApodImage(getBeforeYesterdayDate())
            }
            R.id.chip_yesterday -> {
                getApodImage(getYesterdayDate())
            }
            R.id.chip_today -> {
                getApodImage(getCurrentDate())
            }
        }
        apodBottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun getApodImage(date: String) {
        binding.tvDateOfPicture.text = convertNasaDateFormatToMyFormat(date)
        podViewModel.getPictureOfTheDayRequest(date)
    }

    private fun renderData(state: TabApodState) =
        with(binding) {
            when (state) {
                TabApodState.Loading -> {
                    setVisibilityOnStateLoading(apodSpinKit, youTubePlayerView, apodCoordinator)
                }

                is TabApodState.Success -> {
                    when (state.pictureOfTheDay.mediaType) {
                        MEDIA_TYPE_VIDEO -> {
                            initYouTubeVideoPlayer(
                                state.pictureOfTheDay.url,
                                youTubePlayerView,
                                VIDEO_START_PLAYING_SECOND
                            )
                            setVisibilityOnStateSuccess(apodSpinKit, youTubePlayerView)
                        }
                        MEDIA_TYPE_IMAGE -> {
                            bottomSheetDescriptionHeader.text = state.pictureOfTheDay.title

                            // установка высоты bottomsheet с описанием картинки в зависимости от высоты картинки
                            // по заданному коэффициенту
                            scrollBottomSheetDescription.layoutParams.height =
                                (ivPictureOfTheDay.height * BOTTOMSHEET_PHOTO_DESCRIPTION_HEIGHT_COEFFICIENT).toInt()

                            tvBottomSheetDescription.text = state.pictureOfTheDay.explanation

                            ivPictureOfTheDay.loadWithTransformAndCallback(
                                state.pictureOfTheDay.hdurl,
                                CROSSFADE_DURATION,
                                IMAGE_CORNER_RADIUS
                            ) {
                                if (isAnimationRequired) {
                                    animateApodUI(rootContainer, apodCoordinator)
                                    setVisibilityOnStateSuccess(apodSpinKit)
                                } else {
                                    setVisibilityOnStateSuccess(apodSpinKit, apodCoordinator)
                                }
                            }
                        }

                        else -> {
                            view?.showSnackWithoutAction(getString(R.string.msg_unknown_mediatype))
                        }
                    }
                }

                is TabApodState.Error -> {
                    view?.showSnackWithAction(
                        state.error.message.toString(),
                        getString(R.string.repeat)
                    ) { podViewModel.getPictureOfTheDayRequest() }
                }
            }
        }

    private fun animateApodUI(rootContainer: ConstraintLayout, apodCoordinator: CoordinatorLayout) {
        isAnimationRequired = false

        apodCoordinator.animate()
            .scaleX(0f)
            .scaleY(0f)
            .duration = 0

        ConstraintSet().apply {
            clone(rootContainer)
            connect(R.id.input_layout, ConstraintSet.TOP, R.id.root_container, ConstraintSet.TOP)
            clear(R.id.input_layout, ConstraintSet.BOTTOM)
            connect(
                R.id.chip_group,
                ConstraintSet.BOTTOM,
                R.id.root_container,
                ConstraintSet.BOTTOM
            )
            connect(R.id.chip_group, ConstraintSet.TOP, R.id.apod_coordinator, ConstraintSet.BOTTOM)
            applyTo(rootContainer)
        }

        TransitionManager.beginDelayedTransition(rootContainer, ChangeBounds().apply {
            interpolator = AnticipateOvershootInterpolator(DEFAULT_INTERPOLATOR_TENSION)
            duration = DEFAULT_ANIMATION_DURATION
            addListener(object : Transition.TransitionListener {
                override fun onTransitionStart(transition: Transition) {}

                override fun onTransitionEnd(transition: Transition) {
                    apodCoordinator.isVisible = true
                    apodCoordinator.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setInterpolator(OvershootInterpolator(APOD_COORDINATOR_INTERPOLATOR_TENSION))
                        .duration = APOD_COORDINATOR_ANIMATION_DURATION
                }

                override fun onTransitionCancel(transition: Transition) {}
                override fun onTransitionPause(transition: Transition) {}
                override fun onTransitionResume(transition: Transition) {}
            })
        })
    }

    private fun setBottomSheetCallback() {
        apodBottomSheet.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {}

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.ivBottomSheetArrows.rotation = ARROWS_ROTATION_ANGLE * slideOffset
            }
        })

        binding.bottomSheetContainer.setOnClickListener {
            when (apodBottomSheet.state) {
                BottomSheetBehavior.STATE_EXPANDED -> {
                    apodBottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED

                }
                BottomSheetBehavior.STATE_COLLAPSED -> {
                    apodBottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
                }
                else -> {}
            }
        }
    }
}