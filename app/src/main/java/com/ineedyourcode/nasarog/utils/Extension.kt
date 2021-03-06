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

fun View.showSnackWithoutAction(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}

fun ImageView.loadWithTransform(imagePath: String, crossfadeValue: Int = 0, cornerRadius: Float = 0f) {
    this.load(imagePath) {
        crossfade(true)
        crossfade(crossfadeValue)
        transformations(RoundedCornersTransformation(cornerRadius))
    }
}

fun ImageView.loadWithTransformAndCallback(imagePath: String, crossfadeValue: Int = 0, cornerRadius: Float = 0f, action: () -> Unit) {
    this.load(imagePath) {
        crossfade(true)
        crossfade(crossfadeValue)
        transformations(RoundedCornersTransformation(cornerRadius))
        listener(onSuccess = { _, _ ->
            action()
        }, onError = { _, throwable: Throwable ->
            rootView.showSnackWithoutAction(throwable.message.toString())
        })
    }
}