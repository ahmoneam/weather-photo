package com.moneam.weatherphoto.module.weather.data

import com.ahmoneam.basecleanarchitecture.Result
import com.ahmoneam.basecleanarchitecture.base.platform.IBaseRepository
import com.moneam.weatherphoto.module.weather.domain.Weather

interface IWeatherRepository : IBaseRepository {
    suspend fun getWeather(lat: String, lng: String): Result<Weather>
    suspend fun saveWeatherImage(weatherImage: String): Result<String>
    suspend fun getPhotos(): Result<List<String>>
}
