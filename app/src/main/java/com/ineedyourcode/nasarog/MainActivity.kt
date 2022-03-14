package com.ineedyourcode.nasarog

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.ineedyourcode.nasarog.view.navigation.NavigationFragment
import com.ineedyourcode.nasarog.view.settings.SettingsFragment

private const val KEY_PREFERENCES = "SETTINGS"
private const val KEY_CURRENT_THEME = "CURRENT_THEME"
private const val KEY_FORCED_MODE_NIGHT = "FORCED_DARK_MODE"

class MainActivity : AppCompatActivity() {

    private lateinit var settingsPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        settingsPrefs = getSharedPreferences(KEY_PREFERENCES, MODE_PRIVATE)

        if (getCurrentTheme() == 0) {
            settingsPrefs.edit { putInt(KEY_CURRENT_THEME, R.style.Theme_NASArog) }
        }

        setTheme(getCurrentTheme())

        if (!settingsPrefs.contains(KEY_FORCED_MODE_NIGHT)) {
            setIsDarkMode(false)
        }

        when (getIsDarkMode()) {
            true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

//        if (savedInstanceState == null) {
//            supportFragmentManager
//                .beginTransaction()
//                .replace(R.id.main_fragment_container, SettingsFragment())
//                .commit()
//        }
    }

    fun setCurrentTheme(theme: Int) {
        settingsPrefs.edit { putInt(KEY_CURRENT_THEME, theme) }
    }

    fun getCurrentTheme(): Int {
        return settingsPrefs.getInt(KEY_CURRENT_THEME, 0)
    }

    fun setIsDarkMode(isDarkMode: Boolean) {
        settingsPrefs.edit { putBoolean(KEY_FORCED_MODE_NIGHT, isDarkMode) }
    }

    fun getIsDarkMode(): Boolean {
        return settingsPrefs.getBoolean(KEY_FORCED_MODE_NIGHT, false)
    }
}