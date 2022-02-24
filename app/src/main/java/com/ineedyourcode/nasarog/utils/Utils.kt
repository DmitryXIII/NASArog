package com.ineedyourcode.nasarog.utils

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

fun convertDateFormat(vdate: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val date = inputFormat.parse(vdate)
    return outputFormat.format(date?.time)
}