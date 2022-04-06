package com.ineedyourcode.nasarog

import android.animation.ObjectAnimator
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AnticipateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.animation.doOnEnd
import androidx.core.content.edit
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.ineedyourcode.nasarog.view.ui.navigation.NavigationFragment

private const val KEY_PREFERENCES = "SETTINGS"
private const val KEY_CURRENT_THEME = "CURRENT_THEME"
private const val KEY_FORCED_MODE_NIGHT = "FORCED_DARK_MODE"

class MainActivity : AppCompatActivity() {

    private lateinit var settingsPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                splashScreenView.animate()
                    .scaleX(5f)
                    .scaleY(5f)
                    .alpha(0f)
                    .setInterpolator(AccelerateInterpolator())
                    .withEndAction { splashScreenView.remove() }
                    .duration = 300
            }
        }

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_fragment_container, NavigationFragment())
                .commit()
        }

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