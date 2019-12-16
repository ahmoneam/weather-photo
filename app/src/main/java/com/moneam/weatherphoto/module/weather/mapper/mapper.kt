package com.moneam.weatherphoto.module.weather.mapper

import com.moneam.weatherphoto.module.weather.domain.Weather
import com.moneam.weatherphoto.module.weather.entities.response.WeatherResponse
import com.moneam.weatherphoto.module.weather.entities.view.WeatherDetailsView

fun WeatherResponse.toWeather(): Weather {
    return Weather(
        id = id,
        description = weather.firstOrNull()?.description ?: "",
        icon = weather.firstOrNull()?.icon ?: "",
        temp = main.temp,
        humidity = main.humidity,
        pressure = main.pressure,
        visibility = visibility,
        cityName = name,
        country = sys.country
    )
}

fun Weather.toWeatherDetailsView(image: String): WeatherDetailsView {
    return WeatherDetailsView(
        id = id,
        description = description,
        icon = icon,
        temp = temp,
        humidity = humidity,
        pressure = pressure,
        visibility = visibility,
        cityName = cityName,
        country = country,
        image = image
    )
}

fun WeatherDetailsView.toWeather(): Weather {
    return Weather(
        id = id,
        description = description,
        icon = icon,
        temp = temp,
        humidity = humidity,
        pressure = pressure,
        visibility = visibility,
        cityName = cityName,
        country = country,
        image = image
    )
}
