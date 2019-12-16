package com.moneam.weatherphoto.common.data.remote.api

import com.moneam.weatherphoto.BuildConfig
import com.moneam.weatherphoto.common.data.remote.APIS
import com.moneam.weatherphoto.module.weather.entities.response.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherApi {
    @GET(APIS.URL.WEATHER)
    suspend fun getWeather(
        @Query("lat") lat: String,
        @Query("lon") lng: String,
        @Query("units") units: String = "metric",
        @Query("appid") appId: String = BuildConfig.API_KEY
    ): Response<WeatherResponse>
}