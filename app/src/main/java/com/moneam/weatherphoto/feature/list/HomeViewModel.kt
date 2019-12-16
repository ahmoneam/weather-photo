package com.moneam.weatherphoto.feature.list

import androidx.lifecycle.MutableLiveData
import com.ahmoneam.basecleanarchitecture.base.platform.BaseViewModel
import com.moneam.weatherphoto.module.weather.usecases.GetWeatherPhotosUseCase

class HomeViewModel(private val getWeatherPhotosUseCase: GetWeatherPhotosUseCase) :
    BaseViewModel() {
    val photos = MutableLiveData<List<String>>()

    fun start() {
        wrapBlockingOperation {
            handleResult(getWeatherPhotosUseCase()) {
                photos.value = it.data
            }
        }
    }
}
