package com.ineedyourcode.nasarog.utils

import android.content.Context
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.ineedyourcode.nasarog.R
import java.text.DateFormat
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

fun showToast(context: Context, message: String){
    Toast.makeText(context, message, Toast.LENGTH_SHORT)
        .show()
}