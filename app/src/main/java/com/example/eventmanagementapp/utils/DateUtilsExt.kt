package com.example.eventmanagementapp.utils

import java.text.SimpleDateFormat
import java.util.Locale

fun convert24HourTo12Hour(time: String): String {
    // Create a SimpleDateFormat object for parsing the 24-hour time
    val sdf24 = SimpleDateFormat("HH:mm", Locale.getDefault())
    // Create a SimpleDateFormat object for formatting the 12-hour time
    val sdf12 = SimpleDateFormat("hh:mm a", Locale.getDefault())

    // Parse the input 24-hour time and format it to 12-hour time
    val date = sdf24.parse(time)
    return date?.let { sdf12.format(it) }.toString()
}