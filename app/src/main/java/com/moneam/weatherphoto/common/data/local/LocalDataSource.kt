package com.moneam.weatherphoto.common.data.local

import android.content.Context
import com.ahmoneam.basecleanarchitecture.base.data.local.SharedPreferencesInterface
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LocalDataSource(
    override val sharedPreferences: SharedPreferencesInterface,
    private val context: Context
) : ILocalDataSource {
    companion object {
        const val KEY_WEATHER_IMAGES = "weather-images"
    }

    override fun addWeatherImage(imageUri: String) {
        with(sharedPreferences) {
            getString(KEY_WEATHER_IMAGES)?.let {
                val fromJson = gson.fromJson<List<String>>(it)
                val list = fromJson.toMutableList()
                list.add(imageUri)
                putString(
                    KEY_WEATHER_IMAGES,
                    gson.toJson(list)
                )
            } ?: run {
                putString(
                    KEY_WEATHER_IMAGES,
                    gson.toJson(mutableListOf(imageUri))
                )
            }
        }
    }

    override fun getPhotos(): List<String> {
        return with(sharedPreferences) {
            getString(KEY_WEATHER_IMAGES)?.let {
                return gson.fromJson<List<String>>(it)
            } ?: emptyList<String>()
        }
    }

    inline fun <reified T> Gson.fromJson(json: String) =
        this.fromJson<T>(json, object : TypeToken<T>() {}.type)
}