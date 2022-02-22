package com.ineedyourcode.nasarog.view.picoftheday

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.ineedyourcode.nasarog.databinding.FragmentPictureOfTheDayBinding
import com.ineedyourcode.nasarog.view.BaseBindingFragment
import com.ineedyourcode.nasarog.viewmodel.PictureOfTheDayState
import com.ineedyourcode.nasarog.viewmodel.PictureOfTheDayViewModel

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

        viewModel.getLiveData().observe(viewLifecycleOwner) {
            renderData(it)
        }
        viewModel.getPictureOfTheDayRequest()
    }

    private fun renderData(state: PictureOfTheDayState) {
        when (state) {
            is PictureOfTheDayState.Error -> {
                // TODO
            }
            is PictureOfTheDayState.Loading -> {
                // TODO
            }
            is PictureOfTheDayState.Success -> {
                binding.ivPictureOfTheDay.load(state.pictureOfTheDay.url)
            }
        }
    }
}