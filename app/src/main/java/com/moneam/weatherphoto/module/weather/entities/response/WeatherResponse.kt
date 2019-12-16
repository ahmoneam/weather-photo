package com.moneam.weatherphoto.module.weather.entities.response

import com.google.gson.annotations.SerializedName


data class WeatherResponse(
    @SerializedName("id") val id: String,
    @SerializedName("main") val main: Main,
    @SerializedName("name") val name: String,
    @SerializedName("sys") val sys: Sys,
    @SerializedName("visibility") val visibility: String,
    @SerializedName("weather") val weather: List<WeatherDetails>
) {
    data class Main(
        @SerializedName("humidity") val humidity: String,
        @SerializedName("pressure") val pressure: String,
        @SerializedName("temp") val temp: String
    )

    data class Sys(
        @SerializedName("country") val country: String
    )

    data class WeatherDetails(
        @SerializedName("description") val description: String,
        @SerializedName("icon") val icon: String
    )
}