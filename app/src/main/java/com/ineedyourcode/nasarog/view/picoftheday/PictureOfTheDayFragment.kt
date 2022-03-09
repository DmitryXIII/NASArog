package com.ineedyourcode.nasarog.view.picoftheday

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
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

    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this).get(PictureOfTheDayViewModel::class.java)
    }

    companion object {
        fun newInstance() = PictureOfTheDayFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vvApod.isVisible = false
        binding.apodCoordinator.isVisible = false

        lifecycle.addObserver(binding.vvApod)

        setChips()

        viewModel.getLiveData().observe(viewLifecycleOwner) {
            renderData(it)
        }

        // для тестирования воспроизведения видео - viewModel.getPictureOfTheDayRequest("2022-02-09")
        viewModel.getPictureOfTheDayRequest()

        binding.tvDateOfPicture.text = convertNasaDateFormatToMyFormat(getCurrentDate())

        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent(Intent(Intent.ACTION_VIEW))).apply {
                data = Uri.parse("$WIKI_URL${binding.inputEditText.text}")
            })
        }
    }

    private fun setChips() {
        binding.chipGroup.setOnCheckedChangeListener { _, checkedId ->
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
        }
    }

    private fun getApodImage(date: String) {
        binding.tvDateOfPicture.text = convertNasaDateFormatToMyFormat(date)
        viewModel.getPictureOfTheDayRequest(date)
    }

    private fun renderData(state: PictureOfTheDayState) {
        when (state) {
            is PictureOfTheDayState.Error -> {
                view?.showSnackWithAction(
                    state.error.localizedMessage ?: "",
                    getString(R.string.repeat)
                ) { viewModel.getPictureOfTheDayRequest() }
            }
            is PictureOfTheDayState.Loading -> {
                with(binding) {
                    changeOnStateLoadingVisibility(apodSpinKit, vvApod, apodCoordinator)
                }
            }
            is PictureOfTheDayState.Success -> {
                when (state.pictureOfTheDay.mediaType) {
                    MEDIA_TYPE_VIDEO -> {
                        initYouTubeVideoPlayer(state.pictureOfTheDay.url)
                        changeVisibility(binding.apodSpinKit, binding.vvApod)
                    }
                    MEDIA_TYPE_IMAGE -> {
                        with(binding) {
                            bottomSheetDescriptionHeader.text = state.pictureOfTheDay.title

                            // установка высоты лэйаута с описанием картинки в зависимости от высоты картинки
                            // по заданному коэффициенту
                            scrollBottomSheetDescription.layoutParams.height =
                                (ivPictureOfTheDay.height * BOTTOMSHEET_PHOTO_DESCRIPTION_HEIGHT_COEFFICIENT).toInt()

                            tvBottomSheetDescription.text = state.pictureOfTheDay.explanation
                            ivPictureOfTheDay.loadWithTransform(
                                state.pictureOfTheDay.hdurl,
                                CROSSFADE_DURATION,
                                IMAGE_CORNER_RADIUS
                            )
                            changeVisibility(binding.apodSpinKit, apodCoordinator)
                        }
                    }
                }
            }
        }
    }

    private fun initYouTubeVideoPlayer(url: String) {
        binding.vvApod.addYouTubePlayerListener(object :
            AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(getYouTubeVideoIdFromUrl(url), 0f)
            }
        })
    }

    private fun getYouTubeVideoIdFromUrl(url: String): String =
        url.substringAfterLast('/').substringBefore('?')
}