package com.ineedyourcode.nasarog.utils

import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import com.google.android.material.snackbar.Snackbar

fun View.showSnackWithAction(message: String, actionText: String, action: (View) -> Unit) {
    Snackbar.make(this, message, Snackbar.LENGTH_INDEFINITE)
        .setAction(actionText, action).apply {
            show()
        }
}