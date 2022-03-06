package com.ineedyourcode.nasarog.utils

import android.view.View
import android.widget.ImageView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.google.android.material.snackbar.Snackbar

fun View.showSnackWithAction(message: String, actionText: String, action: (View) -> Unit) {
    Snackbar.make(this, message, Snackbar.LENGTH_INDEFINITE)
        .setAction(actionText, action).apply {
            show()
        }
}

fun ImageView.loadWithTransform(imagePath: String, crossfadeValue: Int, cornerRadius: Float){
    this.load(imagePath){
        crossfade(crossfadeValue)
        transformations(RoundedCornersTransformation(cornerRadius))
        build()
    }
}