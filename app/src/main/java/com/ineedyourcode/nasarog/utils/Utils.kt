package com.ineedyourcode.nasarog.utils

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import java.text.SimpleDateFormat
import java.util.*

fun getBeforeYesterdayDate(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val beforeYesterdayDate = Calendar.getInstance().apply {
        add(Calendar.DATE, -2)
    }
    return dateFormat.format(beforeYesterdayDate.time)
}

fun getYesterdayDate(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val beforeYesterdayDate = Calendar.getInstance().apply {
        add(Calendar.DATE, -1)
    }
    return dateFormat.format(beforeYesterdayDate.time)
}

fun getCurrentDate(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val currentDate = Calendar.getInstance()
    return dateFormat.format(currentDate.time)
}

fun getPreviousDate(dateIndex: Int): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val beforeYesterdayDate = Calendar.getInstance().apply {
        add(Calendar.DATE, dateIndex)
    }
    return dateFormat.format(beforeYesterdayDate.time)
}

fun convertNasaDateFormatToMyFormat(mDate: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val date = inputFormat.parse(mDate)
    return outputFormat.format(date?.time)
}

fun convertMyDateFormatToNasaFormat(mDate: String): String {
    val inputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = inputFormat.parse(mDate)
    return outputFormat.format(date?.time)
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT)
        .show()
}

fun setVisibilityOnStateSuccess(vararg views: View) {
    for (view in views) {
        if (view is ProgressBar) {
            view.isVisible = false
        } else {
            view.visibility = View.VISIBLE
        }
    }
}

fun setVisibilityOnStateLoading(vararg views: View) {
    for (view in views) {
        if (view is ProgressBar) {
            view.isVisible = true
        } else {
            view.visibility = View.INVISIBLE
        }
    }
}

fun getYouTubeVideoIdFromUrl(url: String): String =
    url.substringAfterLast('/').substringBefore('?')

fun initYouTubeVideoPlayer(
    url: String,
    youTubePlayerView: YouTubePlayerView,
    startPlayingSecond: Float
) =
    youTubePlayerView.addYouTubePlayerListener(object :
        AbstractYouTubePlayerListener() {
        override fun onReady(youTubePlayer: YouTubePlayer) {
            youTubePlayer.loadVideo(getYouTubeVideoIdFromUrl(url), startPlayingSecond)
        }
    })