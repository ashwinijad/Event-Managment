package com.example.eventmanagementapp.utils

import androidx.room.TypeConverter
import com.google.gson.Gson

class Converters {

    /**
     * Converts a list of strings to a JSON string representation.
     */
    @TypeConverter
    fun listToJsonString(value: MutableList<String>): String = Gson().toJson(value)

    /**
     * Converts a JSON string back into a list of strings.
     */
    @TypeConverter
    fun jsonStringToList(value: String): List<String> = Gson().fromJson(value, Array<String>::class.java).toList()
}
