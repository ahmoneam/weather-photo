package com.moneam.weatherphoto.common.data.remote

import com.ahmoneam.basecleanarchitecture.base.data.remote.RemoteDataSource
import com.moneam.weatherphoto.common.data.remote.api.WeatherApi


interface IRemoteDataSource : RemoteDataSource {
    val weatherApi: WeatherApi
}