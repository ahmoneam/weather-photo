package com.moneam.weatherphoto.module.weather.usecases

import com.ahmoneam.basecleanarchitecture.Result
import com.ahmoneam.basecleanarchitecture.base.platform.BaseUseCase
import com.moneam.weatherphoto.module.weather.data.IWeatherRepository

class SaveWeatherImageUseCase(repository: IWeatherRepository) :
    BaseUseCase<IWeatherRepository>(repository) {
    suspend operator fun invoke(weatherImage: String): Result<String> {
        return repository.saveWeatherImage(weatherImage)
    }
}
