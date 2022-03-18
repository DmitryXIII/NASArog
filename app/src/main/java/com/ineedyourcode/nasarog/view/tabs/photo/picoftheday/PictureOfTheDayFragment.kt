package com.ineedyourcode.nasarog.view.tabs.photo.picoftheday

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.ineedyourcode.nasarog.R
import com.ineedyourcode.nasarog.databinding.FragmentPictureOfTheDayBinding
import com.ineedyourcode.nasarog.utils.*
import com.ineedyourcode.nasarog.view.BaseBindingFragment
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener

private const val WIKI_URL = "https://ru.wikipedia.org/wiki/"
private const val CROSSFADE_DURATION = 500
private const val IMAGE_CORNER_RADIUS = 25f
private const val BOTTOMSHEET_PHOTO_DESCRIPTION_HEIGHT_COEFFICIENT = 0.6
private const val MEDIA_TYPE_VIDEO = "video"
private const val MEDIA_TYPE_IMAGE = "image"

class PictureOfTheDayFragment :
    BaseBindingFragment<FragmentPictureOfTheDayBinding>(FragmentPictureOfTheDayBinding::inflate) {

    private lateinit var apodBottomSheet: BottomSheetBehavior<ConstraintLayout>

    private val podViewModel by viewModels<PictureOfTheDayViewModel>()

    companion object {
        fun newInstance() = PictureOfTheDayFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vvApod.isVisible = false
        binding.apodCoordinator.isVisible = false

        lifecycle.addObserver(binding.vvApod)

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

    private fun renderData(state: PictureOfTheDayState) = with(binding) {
        when (state) {
            is PictureOfTheDayState.Error -> {
                view?.showSnackWithAction(
                    state.error.message.toString(),
                    getString(R.string.repeat)
                ) { podViewModel.getPictureOfTheDayRequest() }
            }

            is PictureOfTheDayState.Loading -> {
                setVisibilityOnStateLoading(apodSpinKit, vvApod, apodCoordinator)
            }

            is PictureOfTheDayState.Success -> {
                when (state.pictureOfTheDay.mediaType) {
                    MEDIA_TYPE_VIDEO -> {
                        initYouTubeVideoPlayer(state.pictureOfTheDay.url)
                        setVisibilityOnStateSuccess(apodSpinKit, vvApod)
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
                            setVisibilityOnStateSuccess(apodSpinKit, apodCoordinator)
                        }
                    }
                    else -> {
                        view?.showSnackWithoutAction(getString(R.string.msg_unknown_mediatype))
                    }
                }
            }
        }
    }

    private fun initYouTubeVideoPlayer(url: String) = binding.vvApod.addYouTubePlayerListener(object :
            AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(getYouTubeVideoIdFromUrl(url), 0f)
            }
        })

    private fun getYouTubeVideoIdFromUrl(url: String): String =
        url.substringAfterLast('/').substringBefore('?')

    private fun setBottomSheetCallback() {
        apodBottomSheet.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {}

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.ivBottomSheetArrows.rotation = 180 * slideOffset
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