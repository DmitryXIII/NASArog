package com.ineedyourcode.nasarog

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.ineedyourcode.nasarog.view.navigation.NavigationFragment

private const val KEY_PREFERENCES = "SETTINGS"
private const val KEY_CURRENT_THEME = "CURRENT_THEME"

class MainActivity : AppCompatActivity() {

    private lateinit var settingsPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContentView(R.layout.activity_main)

        settingsPrefs = getSharedPreferences(KEY_PREFERENCES, MODE_PRIVATE)

        setTheme(getCurrentTheme())

        if (getCurrentTheme() == 0) {
            settingsPrefs.edit { putInt(KEY_CURRENT_THEME, R.style.Theme_NASArog) }
        }

        if (savedInstanceState == null) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_fragment_container, NavigationFragment())
                .commit()
        }
    }

    fun setCurrentTheme(theme: Int) {
        settingsPrefs.edit { putInt(KEY_CURRENT_THEME, theme) }
    }

    fun getCurrentTheme(): Int {
        return settingsPrefs.getInt(KEY_CURRENT_THEME, 0)
    }
}