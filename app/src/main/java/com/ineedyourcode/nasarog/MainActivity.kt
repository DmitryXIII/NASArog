package com.ineedyourcode.nasarog

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ineedyourcode.nasarog.view.picoftheday.PictureOfTheDayFragment

private const val KEY_PREFERENCES = "SETTINGS"
private const val KEY_CURRENT_THEME = "CURRENT_THEME"

class MainActivity : AppCompatActivity() {

    private lateinit var sp: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sp = getSharedPreferences(KEY_PREFERENCES, MODE_PRIVATE)
        editor = sp.edit()

        setTheme(getCurrentTheme())

        if (getCurrentTheme() == 0) {
            editor.putInt(KEY_CURRENT_THEME, R.style.Theme_NASArog)
            editor.apply()
        }

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_fragment_container, PictureOfTheDayFragment.newInstance())
                .commit()
        }
    }

    fun setCurrentTheme(theme: Int) {
        editor.putInt(KEY_CURRENT_THEME, theme)
        editor.apply()
    }

    fun getCurrentTheme(): Int {
        return sp.getInt(KEY_CURRENT_THEME, 0)
    }
}