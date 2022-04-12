package com.ineedyourcode.nasarog.view.ui.settings

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.ineedyourcode.nasarog.MainActivity
import com.ineedyourcode.nasarog.R
import com.ineedyourcode.nasarog.databinding.FragmentSettingsBinding
import com.ineedyourcode.nasarog.view.basefragment.BaseFragment

class SettingsFragment :
    BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = requireActivity() as MainActivity

        when (activity.getCurrentTheme()) {
            R.style.Theme_NASArog -> binding.radioGroup.check(R.id.check1)
            R.style.Theme_NASArog_2 -> binding.radioGroup.check(R.id.check2)
        }

        binding.checkboxDarkTheme.apply {
            isChecked = activity.getIsDarkMode()

            setOnCheckedChangeListener { _, isChecked ->
                when (isChecked) {
                    true -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        activity.setIsDarkMode(true)
                    }
                    false -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        activity.setIsDarkMode(false)
                    }
                }
            }
        }

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.check1 -> {
                    activity.setCurrentTheme(R.style.Theme_NASArog)
                    activity.recreate()
                }
                R.id.check2 -> {
                    activity.setCurrentTheme(R.style.Theme_NASArog_2)
                    activity.recreate()
                }
            }
        }
    }
}