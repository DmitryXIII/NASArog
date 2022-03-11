package com.ineedyourcode.nasarog.view.settings

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.ineedyourcode.nasarog.MainActivity
import com.ineedyourcode.nasarog.R
import com.ineedyourcode.nasarog.databinding.FragmentSettingsBinding
import com.ineedyourcode.nasarog.view.BaseBindingFragment

class SettingsFragment :
    BaseBindingFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = requireActivity() as MainActivity

        when (activity.getCurrentTheme()) {
            R.style.Theme_NASArog -> binding.radioGroup.check(R.id.check1)
            R.style.Theme_NASArog_2 -> binding.radioGroup.check(R.id.check2)
        }

        binding.checkboxDarkTheme.apply {
            isChecked = AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_NO

            setOnCheckedChangeListener { _, isChecked ->
                when (isChecked) {
                    true -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                    false -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
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