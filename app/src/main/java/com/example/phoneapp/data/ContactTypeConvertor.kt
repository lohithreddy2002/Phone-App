package com.example.phoneapp.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class ContactTypeConvertor {
    private val gson = Gson()

    @TypeConverter
    fun stringToList(data: String?): ArrayList<String?>? {
        if (data == null) {
            return arrayListOf()
        }
        val listType: Type = object : TypeToken<ArrayList<String?>?>() {}.type
        return gson.fromJson<ArrayList<String?>>(data, listType)
    }

    @TypeConverter
    fun listToString(someObjects: List<String?>?): String? {
        return gson.toJson(someObjects)
    }
}