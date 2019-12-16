package com.moneam.weatherphoto.feature.photo

import androidx.lifecycle.MutableLiveData
import com.ahmoneam.basecleanarchitecture.base.platform.BaseViewModel
import com.ahmoneam.basecleanarchitecture.utils.Event
import com.moneam.weatherphoto.module.weather.domain.Weather
import com.moneam.weatherphoto.module.weather.entities.view.WeatherDetailsView
import com.moneam.weatherphoto.module.weather.mapper.toWeatherDetailsView
import com.moneam.weatherphoto.module.weather.usecases.GetWeatherUseCase
import com.moneam.weatherphoto.module.weather.usecases.SaveWeatherImageUseCase

class PhotoViewModel(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val saveWeatherImageUseCase: SaveWeatherImageUseCase
) : BaseViewModel() {
    private lateinit var _weather: Weather

    val weather = MutableLiveData<WeatherDetailsView>()

    val startCamera = MutableLiveData<Event<Unit>>()

    val enableShare = MutableLiveData<Boolean>().apply { value = false }

    fun updateLocation(latitude: Double, longitude: Double) {
        startCamera.postValue(Event(Unit))
        wrapBlockingOperation {
            handleResult(getWeatherUseCase(latitude.toString(), longitude.toString())) {
                _weather = it.data
                startCamera.postValue(Event(Unit))
            }
        }
    }

    fun imageUpdated(image: String) {
        val weatherDetailsView = _weather.toWeatherDetailsView(image)
        weather.value = weatherDetailsView
        enableShare.value = true
    }

    fun onShareImageClick(uri: String) {
        wrapBlockingOperation {
            handleResult(saveWeatherImageUseCase(uri)) {
                // pass
            }
        }
    }
}
