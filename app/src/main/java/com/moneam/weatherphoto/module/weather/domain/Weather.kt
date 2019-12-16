package com.moneam.weatherphoto.module.weather.domain

data class Weather(
    val id: String,
    val description: String,
    val icon: String,
    val temp: String,
    val humidity: String,
    val pressure: String,
    val visibility: String,
    val cityName: String,
    val country: String,
    val image: String? = null
)