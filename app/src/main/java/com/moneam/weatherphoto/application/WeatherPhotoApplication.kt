package com.moneam.weatherphoto.application

import com.ahmoneam.basecleanarchitecture.base.BaseApplication
import com.moneam.weatherphoto.BuildConfig
import com.moneam.weatherphoto.di.FeaturesKoinModules
import okhttp3.Interceptor

class WeatherPhotoApplication : BaseApplication() {
    override val modulesList = FeaturesKoinModules.modules
    override val interceptors: List<Interceptor> = listOf()
    override val baseUrl = BuildConfig.BASE_URL
    override val sharedPreferencesName = "weather_photo"
    override val isDebug = BuildConfig.DEBUG
}