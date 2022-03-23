package com.ineedyourcode.nasarog.view.tabspager.tabs.apod

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.ineedyourcode.nasarog.R
import com.ineedyourcode.nasarog.databinding.FragmentTabApodBinding
import com.ineedyourcode.nasarog.utils.*
import com.ineedyourcode.nasarog.view.basefragment.BaseFragment
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener

private const val WIKI_URL = "https://ru.wikipedia.org/wiki/"
private const val CROSSFADE_DURATION = 500
private const val IMAGE_CORNER_RADIUS = 25f
private const val BOTTOMSHEET_PHOTO_DESCRIPTION_HEIGHT_COEFFICIENT = 0.75
private const val MEDIA_TYPE_VIDEO = "video"
private const val MEDIA_TYPE_IMAGE = "image"
private const val ARROWS_ROTATION_ANGLE = 180
private const val VIDEO_START_PLAYING_SECOND = 0f
private const val ANIMATION_DURATION = 1000L
private const val INTERPOLATOR_TENSION = 1.5f

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

        binding.videoApod.isVisible = false
        binding.apodCoordinator.isVisible = false

        lifecycle.addObserver(binding.videoApod)

        setChips()

        podViewModel.getLiveData().observe(viewLifecycleOwner) {
            renderData(it, savedInstanceState)
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

    private fun renderData(state: TabApodState, savedInstanceState: Bundle?) =
        with(binding) {
            when (state) {
                TabApodState.Loading -> {
                    setVisibilityOnStateLoading(apodSpinKit, videoApod, apodCoordinator)
                }

                is TabApodState.Success -> {
                    when (state.pictureOfTheDay.mediaType) {
                        MEDIA_TYPE_VIDEO -> {
                            initYouTubeVideoPlayer(state.pictureOfTheDay.url)
                            setVisibilityOnStateSuccess(apodSpinKit, videoApod)
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
                                    animateApodUI(rootContainer)
                                }

                                setVisibilityOnStateSuccess(apodSpinKit, apodCoordinator)
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

    private fun animateApodUI(rootContainer: ConstraintLayout) {
        isAnimationRequired = false

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
            clear(R.id.chip_group, ConstraintSet.TOP)
            applyTo(rootContainer)
        }

        TransitionManager.beginDelayedTransition(rootContainer, ChangeBounds().apply {
            interpolator = AnticipateOvershootInterpolator(INTERPOLATOR_TENSION)
            duration = ANIMATION_DURATION
        })
    }

    private fun initYouTubeVideoPlayer(url: String) =
        binding.videoApod.addYouTubePlayerListener(object :
            AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(getYouTubeVideoIdFromUrl(url), VIDEO_START_PLAYING_SECOND)
            }
        })

    private fun getYouTubeVideoIdFromUrl(url: String): String =
        url.substringAfterLast('/').substringBefore('?')

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