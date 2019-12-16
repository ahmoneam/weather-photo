package com.moneam.weatherphoto.module.weather.usecases

import com.ahmoneam.basecleanarchitecture.Result
import com.ahmoneam.basecleanarchitecture.base.platform.BaseUseCase
import com.moneam.weatherphoto.module.weather.data.IWeatherRepository
import com.moneam.weatherphoto.module.weather.domain.Weather

class GetWeatherUseCase(repository: IWeatherRepository) :
    BaseUseCase<IWeatherRepository>(repository) {
    suspend operator fun invoke(lat: String, lng: String): Result<Weather> {
        return return repository.getWeather(lat, lng)
    }
}