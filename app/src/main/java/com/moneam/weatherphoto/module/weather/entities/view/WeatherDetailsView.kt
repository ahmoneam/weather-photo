package com.moneam.weatherphoto.module.weather.entities.view

data class WeatherDetailsView(
    val id: String,
    val description: String,
    val icon: String,
    val temp: String,
    val humidity: String,
    val pressure: String,
    val visibility: String,
    val cityName: String,
    val country: String,
    val address: String = "$cityName, $country",
    val image: String? = null
)