package com.moneam.weatherphoto.di

import com.ahmoneam.basecleanarchitecture.base.common.ApplicationResource
import com.ahmoneam.basecleanarchitecture.base.common.IApplicationResource
import com.moneam.weatherphoto.common.data.local.ILocalDataSource
import com.moneam.weatherphoto.common.data.local.LocalDataSource
import com.moneam.weatherphoto.common.data.remote.IRemoteDataSource
import com.moneam.weatherphoto.common.data.remote.RemoteDataSource
import com.moneam.weatherphoto.feature.list.HomeViewModel
import com.moneam.weatherphoto.feature.photo.PhotoViewModel
import com.moneam.weatherphoto.module.weather.data.IWeatherRepository
import com.moneam.weatherphoto.module.weather.data.WeatherRepository
import com.moneam.weatherphoto.module.weather.usecases.GetWeatherPhotosUseCase
import com.moneam.weatherphoto.module.weather.usecases.GetWeatherUseCase
import com.moneam.weatherphoto.module.weather.usecases.SaveWeatherImageUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.experimental.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module


object FeaturesKoinModules {
    val modules: ArrayList<Module>
        get() {
            val list = arrayListOf<Module>()

            // general
            list.add(module {
                // local data source
                single<ILocalDataSource> { LocalDataSource(get(), androidContext()) }
                // remote data source
                single<IRemoteDataSource> { RemoteDataSource(get()) }
                // resource helper
                single<IApplicationResource> { ApplicationResource(androidContext()) }
            })

            list.add(weatherPhotoModule)
            return list
        }
    private val weatherPhotoModule = module {
        // repository
        factory<IWeatherRepository> { WeatherRepository(get(), get()) }

        // use cases
        factory { GetWeatherUseCase(get()) }
        factory { SaveWeatherImageUseCase(get()) }
        factory { GetWeatherPhotosUseCase(get()) }

        // view model
        viewModel<PhotoViewModel>()
        viewModel<HomeViewModel>()
    }
}