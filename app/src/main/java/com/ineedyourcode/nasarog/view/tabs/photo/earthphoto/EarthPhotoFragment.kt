package com.ineedyourcode.nasarog.view.tabs.photo.earthphoto

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.ineedyourcode.nasarog.BuildConfig
import com.ineedyourcode.nasarog.R
import com.ineedyourcode.nasarog.databinding.FragmentEarthPhotoBinding
import com.ineedyourcode.nasarog.utils.*
import com.ineedyourcode.nasarog.view.BaseBindingFragment

private const val CROSSFADE_DURATION = 1000
private const val IMAGE_CORNER_RADIUS = 25f
private const val BASE_EARTH_PHOTO_URL = "https://api.nasa.gov/EPIC/archive/natural/"

class EarthPhotoFragment :
    BaseBindingFragment<FragmentEarthPhotoBinding>(FragmentEarthPhotoBinding::inflate) {

    private val listOfDates = mutableListOf<String>()
    private val viewModel: EarthPhotoViewModel by lazy {
        ViewModelProvider(this).get(EarthPhotoViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getLiveData().observe(viewLifecycleOwner) {
            renderData(it)
        }

        viewModel.getEarthPhotoDatesRequest()
    }

    private fun renderData(state: EarthPhotoState) {
        with(binding) {
            when (state) {
                is EarthPhotoState.Error -> {
                    view?.showSnackWithoutAction(state.error.message.toString())
                }
                EarthPhotoState.Loading -> {
                    setVisibilityOnStateLoading(earthPhotoSpinKit, groupEarthPhoto)
                }

                is EarthPhotoState.PhotoSuccess -> {
                    tvDateEarthPhoto.text =
                        convertNasaDateFormatToMyFormat(state.earthPhoto.date)
                    ivEarthPhoto.loadWithTransformAndCallback(
                        (convertDateToEarthUrlDateFormat(
                            state.earthPhoto.date,
                            state.earthPhoto.image
                        )),
                        CROSSFADE_DURATION,
                        IMAGE_CORNER_RADIUS
                    ) {
                        setVisibilityOnStateSuccess(earthPhotoSpinKit, groupEarthPhoto)
                    }
                }
                is EarthPhotoState.DatesSuccess -> {
                    for (date in state.mListOfDates) {
                        listOfDates.add(convertNasaDateFormatToMyFormat(date.date))
                    }

                    spinnerDateEarthPhoto.visibility = View.VISIBLE

                    spinnerDateEarthPhoto.adapter =
                        ArrayAdapter(requireContext(), R.layout.spinner_item, listOfDates).apply {
                            setDropDownViewResource(R.layout.spinner_dropdown_item)
                        }

                    spinnerDateEarthPhoto.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(parent: AdapterView<*>?) {}

                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                viewModel.getEarthPhotoRequest(
                                    convertMyDateFormatToNasaFormat(
                                        parent?.getItemAtPosition(position).toString()
                                    )
                                )
                            }
                        }
                }
            }
        }
    }

    private fun convertDateToEarthUrlDateFormat(date: String, imageId: String) =
        "$BASE_EARTH_PHOTO_URL${
            date.substring(0, 10).replace('-', '/')
        }/png/$imageId.png?api_key=${BuildConfig.NASA_API_KEY}"
}