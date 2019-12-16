package com.moneam.weatherphoto.common.data.local

import com.ahmoneam.basecleanarchitecture.base.data.local.LocalDataSource

interface ILocalDataSource : LocalDataSource {
    fun addWeatherImage(imageUri: String)
    fun getPhotos(): List<String>
}