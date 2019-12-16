package com.moneam.weatherphoto.common.data.remote

import com.ahmoneam.basecleanarchitecture.base.data.remote.RemoteDataSourceImpl
import com.moneam.weatherphoto.common.data.remote.api.WeatherApi
import retrofit2.Retrofit

class RemoteDataSource(retrofit: Retrofit) : RemoteDataSourceImpl(retrofit),
    IRemoteDataSource {
    override val weatherApi: WeatherApi = retrofit.create(WeatherApi::class.java)

}