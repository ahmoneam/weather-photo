package com.moneam.weatherphoto.di

import com.ahmoneam.basecleanarchitecture.base.common.ApplicationResource
import com.ahmoneam.basecleanarchitecture.base.common.IApplicationResource
import com.moneam.weatherphoto.common.data.local.ILocalDataSource
import com.moneam.weatherphoto.common.data.local.LocalDataSource
import com.moneam.weatherphoto.common.data.remote.IRemoteDataSource
import com.moneam.weatherphoto.common.data.remote.RemoteDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module


object FeaturesKoinModules {
    val modules: ArrayList<Module>
        get() {
            val list = arrayListOf<Module>()

            // general
            list.add(module {
                // local data source
                single<ILocalDataSource> { LocalDataSource(get()) }
                // remote data source
                single<IRemoteDataSource> { RemoteDataSource(get()) }
                // resource helper
                single<IApplicationResource> { ApplicationResource(androidContext()) }
            })
            return list
        }
}