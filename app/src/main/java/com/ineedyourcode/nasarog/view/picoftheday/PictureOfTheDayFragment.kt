package com.ineedyourcode.nasarog.view.picoftheday

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import coil.load
import coil.transform.RoundedCornersTransformation
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.Circle
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.ineedyourcode.nasarog.MainActivity
import com.ineedyourcode.nasarog.R
import com.ineedyourcode.nasarog.databinding.FragmentPictureOfTheDayBinding
import com.ineedyourcode.nasarog.utils.convertDateFormat
import com.ineedyourcode.nasarog.utils.getBeforeYesterdayDate
import com.ineedyourcode.nasarog.utils.getCurrentDate
import com.ineedyourcode.nasarog.utils.getYesterdayDate
import com.ineedyourcode.nasarog.view.BaseBindingFragment
import com.ineedyourcode.nasarog.view.BottomNavigationDrawerFragment
import com.ineedyourcode.nasarog.viewmodel.PictureOfTheDayState
import com.ineedyourcode.nasarog.viewmodel.PictureOfTheDayViewModel

private const val WIKI_URL = "https://ru.wikipedia.org/wiki/"

class PictureOfTheDayFragment :
    BaseBindingFragment<FragmentPictureOfTheDayBinding>(FragmentPictureOfTheDayBinding::inflate) {

    private var isMainScreen: Boolean = true

    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this).get(PictureOfTheDayViewModel::class.java)
    }

    companion object {
        fun newInstance() = PictureOfTheDayFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBottomBar()

        setChips()

        viewModel.getLiveData().observe(viewLifecycleOwner) {
            renderData(it)
        }

        viewModel.getPictureOfTheDayRequest()

        // кастомный прогрессбар
        binding.spinKit.setIndeterminateDrawable(Circle() as Sprite);

        binding.tvDateOfPicture.text = convertDateFormat(getCurrentDate())

        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent(Intent(Intent.ACTION_VIEW))).apply {
                data = Uri.parse("$WIKI_URL${binding.inputEditText.text}")
            })
        }

        BottomSheetBehavior.from(binding.bottomSheetContainer).apply {
            isHideable = false
        }

        binding.fab.setOnClickListener {
            if (isMainScreen) {
                with(binding) {
                    fab.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_arrow_back
                        )
                    )
                    bottomAppBar.apply {
                        fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                        navigationIcon = null
                        replaceMenu(R.menu.menu_bottom_bar_no_main_screen)
                    }
                }
            } else {
                with(binding) {
                    fab.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_plus
                        )
                    )
                    bottomAppBar.apply {
                        fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                        navigationIcon = ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_burger
                        )
                        replaceMenu(R.menu.menu_bottom_bar)
                    }
                }
            }
            isMainScreen = !isMainScreen
        }
    }

    private fun setChips() {
        binding.chipGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.chip_before_yesterday -> {
                    val date = getBeforeYesterdayDate()
                    binding.tvDateOfPicture.text = convertDateFormat(date)
                    viewModel.getPictureOfTheDayFromDateRequest(date)
                }
                R.id.chip_yesterday -> {
                    val date = getYesterdayDate()
                    binding.tvDateOfPicture.text = convertDateFormat(date)
                    viewModel.getPictureOfTheDayFromDateRequest(getYesterdayDate())
                }
                R.id.chip_today -> {
                    binding.tvDateOfPicture.text = convertDateFormat(getCurrentDate())
                    viewModel.getPictureOfTheDayRequest()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_bottombar_favorite -> {
                Toast.makeText(requireContext(), "FAVORITE", Toast.LENGTH_SHORT).show()
            }
            R.id.action_bottombar_settings -> {
                Toast.makeText(requireContext(), "SETTINGS", Toast.LENGTH_SHORT).show()
            }
            android.R.id.home -> {
                BottomNavigationDrawerFragment().show(requireActivity().supportFragmentManager, "")
            }
            R.id.action_bottombar_search -> {
                Toast.makeText(requireContext(), "SEARCH", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun renderData(state: PictureOfTheDayState) {
        when (state) {
            is PictureOfTheDayState.Error -> {
                // TODO
            }
            is PictureOfTheDayState.Loading -> {
                binding.spinKit.isVisible = true
                binding.apodCoordinator.isVisible = false
            }
            is PictureOfTheDayState.Success -> {
                binding.spinKit.isVisible = false
                binding.apodCoordinator.isVisible = true
                binding.bottomSheetDescriptionHeader.text = state.pictureOfTheDay.title
                binding.bottomSheetDescription.text = state.pictureOfTheDay.explanation
                binding.ivPictureOfTheDay.load(state.pictureOfTheDay.hdurl) {
                    crossfade(500)
                    transformations(RoundedCornersTransformation(25F))
                    build()
                }
            }
        }
    }

    private fun setBottomBar() {
        (requireActivity() as MainActivity).setSupportActionBar(binding.bottomAppBar)
        setHasOptionsMenu(true)
    }
}