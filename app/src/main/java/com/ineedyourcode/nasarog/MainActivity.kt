package com.ineedyourcode.nasarog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ineedyourcode.nasarog.view.picoftheday.PictureOfTheDayFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_fragment_container, PictureOfTheDayFragment.newInstance())
                .commit()
        }
    }
}