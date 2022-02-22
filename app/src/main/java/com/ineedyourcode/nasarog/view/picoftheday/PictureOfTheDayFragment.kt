package com.ineedyourcode.nasarog.view.picoftheday

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.ineedyourcode.nasarog.databinding.FragmentPictureOfTheDayBinding
import com.ineedyourcode.nasarog.view.BaseBindingFragment
import com.ineedyourcode.nasarog.viewmodel.PictureOfTheDayState
import com.ineedyourcode.nasarog.viewmodel.PictureOfTheDayViewModel

private const val WIKI_URL = "https://ru.wikipedia.org/wiki/"

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

        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent(Intent(Intent.ACTION_VIEW))).apply {
                data = Uri.parse("$WIKI_URL${binding.inputEditText.text}")
            })
        }

        BottomSheetBehavior.from(binding.bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HALF_EXPANDED
            isHideable = false
        }
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