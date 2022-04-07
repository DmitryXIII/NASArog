package com.ineedyourcode.nasarog

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.ineedyourcode.nasarog.view.ui.navigation.NavigationFragment

private const val KEY_PREFERENCES = "SETTINGS"
private const val KEY_CURRENT_THEME = "CURRENT_THEME"
private const val KEY_FORCED_MODE_NIGHT = "FORCED_DARK_MODE"

class MainActivity : AppCompatActivity() {

    private lateinit var settingsPrefs: SharedPreferences
    private var isSplashScreenKeeping = false

    override fun onCreate(savedInstanceState: Bundle?) {

        if (savedInstanceState == null) {
            isSplashScreenKeeping = true
        }

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                isSplashScreenKeeping
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                setOnExitAnimationListener { viewProvider ->
                    viewProvider.iconView.animate()
                        .scaleX(0f)
                        .scaleY(0f)
                        .duration = 200

                    viewProvider.view.animate()
                        .alpha(0f)
                        .withEndAction {viewProvider.remove()}
                        .duration = 200
                }
            }
        }

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        settingsPrefs = getSharedPreferences(KEY_PREFERENCES, MODE_PRIVATE)

        setTheme(getCurrentTheme())

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_fragment_container, NavigationFragment())
                .commit()
        }

        if (getCurrentTheme() == 0) {
            settingsPrefs.edit { putInt(KEY_CURRENT_THEME, R.style.Theme_NASArog) }
        }

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

    fun closeSplashScreen() {
        isSplashScreenKeeping = false
    }
}