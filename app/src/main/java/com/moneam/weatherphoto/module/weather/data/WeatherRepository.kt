package com.moneam.weatherphoto.module.weather.data

import com.ahmoneam.basecleanarchitecture.Result
import com.ahmoneam.basecleanarchitecture.base.platform.BaseRepository
import com.moneam.weatherphoto.common.data.local.ILocalDataSource
import com.moneam.weatherphoto.common.data.remote.IRemoteDataSource
import com.moneam.weatherphoto.module.weather.domain.Weather
import com.moneam.weatherphoto.module.weather.mapper.toWeather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepository(localDataSource: ILocalDataSource, remoteDataSource: IRemoteDataSource) :
    BaseRepository<ILocalDataSource, IRemoteDataSource>(
        localDataSource,
        remoteDataSource
    ), IWeatherRepository {

    override suspend fun getWeather(lat: String, lng: String): Result<Weather> {
        return when (val result =
            safeApiCall { remoteDataSource.weatherApi.getWeather(lat, lng) }) {
            is Result.Success -> Result.Success(result.data.toWeather())
            is Result.Error -> result
            else -> unexpectedError
        }
    }

    override suspend fun saveWeatherImage(weatherImage: String): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                localDataSource.addWeatherImage(weatherImage)
                Result.Success(weatherImage)
            } catch (error: Throwable) {
                unexpectedError(error)
            }
        }
    }

    override suspend fun getPhotos(): Result<List<String>> {
        return withContext(Dispatchers.IO) {
            try {
                Result.Success(localDataSource.getPhotos())
            } catch (error: Throwable) {
                unexpectedError(error)
            }
        }
    }
}
