package com.ineedyourcode.nasarog.view.earthphoto

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import coil.load
import coil.transform.RoundedCornersTransformation
import com.ineedyourcode.nasarog.BuildConfig
import com.ineedyourcode.nasarog.databinding.FragmentEarthPhotoBinding
import com.ineedyourcode.nasarog.utils.convertDateFormat
import com.ineedyourcode.nasarog.view.BaseBindingFragment


private const val CROSSFADE_DURATION = 1000
private const val IMAGE_CORNER_RADIUS = 25f
private const val BASE_EARTH_PHOTO_URL = "https://api.nasa.gov/EPIC/archive/natural/"

class EarthPhotoFragment :
    BaseBindingFragment<FragmentEarthPhotoBinding>(FragmentEarthPhotoBinding::inflate) {

    private val viewModel: EarthPhotoViewModel by lazy {
        ViewModelProvider(this).get(EarthPhotoViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getLiveData().observe(viewLifecycleOwner) {
            renderData(it)
        }

        viewModel.getEarthPhotoRequest()
    }

    private fun renderData(state: EarthPhotoState) {
        when (state) {
            is EarthPhotoState.Error -> {
                viewModel.getEarthPhotoRequest()
            }
            is EarthPhotoState.Loading -> {
                binding.earthPhotoSpinKit.isVisible = true
                binding.ivEarthPhoto.isVisible = false
            }
            is EarthPhotoState.Success -> {
                binding.tvDateEarthPhoto.text = convertDateFormat(state.earthPhoto.date)
                binding.ivEarthPhoto.load(
                    "$BASE_EARTH_PHOTO_URL${state.earthPhoto.date.substring(0, 10).replace('-', '/')}/png/${state.earthPhoto.image}.png?api_key=${BuildConfig.NASA_API_KEY}"
                ) {
                    crossfade(CROSSFADE_DURATION)
                    transformations(RoundedCornersTransformation(IMAGE_CORNER_RADIUS))
                    build()
                    listener(onSuccess = { _, _ ->

                        binding.earthPhotoSpinKit.isVisible = false
                        binding.ivEarthPhoto.isVisible = true
                    }, onError = { _, throwable: Throwable ->
                        // handle error here
                    })
                }

                Log.d("EARTHPHOTO", state.earthPhoto.image)
            }
        }
    }
}