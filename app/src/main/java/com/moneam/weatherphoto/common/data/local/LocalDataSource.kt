package com.moneam.weatherphoto.common.data.local

import android.content.Context
import com.ahmoneam.basecleanarchitecture.base.data.local.SharedPreferencesInterface
import com.google.gson.reflect.TypeToken

class LocalDataSource(
    override val sharedPreferences: SharedPreferencesInterface,
    private val context: Context
) : ILocalDataSource {
    companion object {
        const val KEY_WEATHER_IMAGES = "weather-images"
    }

    override fun addWeatherImage(imageUri: String) {
        sharedPreferences.getObject(
            KEY_WEATHER_IMAGES,
            genericType<List<String>>()::class
        )
    }

    inline fun <reified T> genericType() = object : TypeToken<T>() {}.type

}