package com.ineedyourcode.nasarog.view.picoftheday

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import coil.load
import coil.transform.RoundedCornersTransformation
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.Circle
import com.ineedyourcode.nasarog.R
import com.ineedyourcode.nasarog.databinding.FragmentPictureOfTheDayBinding
import com.ineedyourcode.nasarog.utils.*
import com.ineedyourcode.nasarog.view.BaseBindingFragment

private const val WIKI_URL = "https://ru.wikipedia.org/wiki/"
private const val CROSSFADE_DURATION = 500
private const val IMAGE_CORNER_RADIUS = 25f
private const val BOTTOMSHEET_PHOTO_DESCRIPTION_HEIGHT_COEFFICIENT = 0.6


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

        setChips()

        viewModel.getLiveData().observe(viewLifecycleOwner) {
            renderData(it)
        }

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
                    val date = getBeforeYesterdayDate()
                    binding.tvDateOfPicture.text = convertNasaDateFormatToMyFormat(date)
                    viewModel.getPictureOfTheDayRequest(date)
                }
                R.id.chip_yesterday -> {
                    val date = getYesterdayDate()
                    binding.tvDateOfPicture.text = convertNasaDateFormatToMyFormat(date)
                    viewModel.getPictureOfTheDayRequest(getYesterdayDate())
                }
                R.id.chip_today -> {
                    binding.tvDateOfPicture.text = convertNasaDateFormatToMyFormat(getCurrentDate())
                    viewModel.getPictureOfTheDayRequest()
                }
            }
        }
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
                binding.apodSpinKit.isVisible = true
                binding.apodCoordinator.isVisible = false
            }
            is PictureOfTheDayState.Success -> {
                with(binding) {
                    bottomSheetDescriptionHeader.text = state.pictureOfTheDay.title

                    // установка высоты лэйаута с описанием картинки в зависимости от высоты картинки
                    // по заданному коэффициенту
                    scrollBottomSheetDescription.layoutParams.height =
                        (ivPictureOfTheDay.height * BOTTOMSHEET_PHOTO_DESCRIPTION_HEIGHT_COEFFICIENT).toInt()

                    tvBottomSheetDescription.text = state.pictureOfTheDay.explanation
                    ivPictureOfTheDay.load(state.pictureOfTheDay.hdurl) {
                        crossfade(CROSSFADE_DURATION)
                        transformations(RoundedCornersTransformation(IMAGE_CORNER_RADIUS))
                        build()
                    }
                    apodSpinKit.isVisible = false
                    apodCoordinator.isVisible = true
                }
            }
        }
    }
}