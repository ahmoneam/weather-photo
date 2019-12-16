package com.ahmoneam.basecleanarchitecture.base.di

import com.ahmoneam.basecleanarchitecture.base.data.local.SharedPreferencesInterface
import com.ahmoneam.basecleanarchitecture.base.data.local.SharedPreferencesUtils
import com.ahmoneam.basecleanarchitecture.base.data.remote.LanguageInterceptor
import com.ahmoneam.basecleanarchitecture.utils.ConnectivityUtils
import com.ahmoneam.basecleanarchitecture.utils.IConnectivityUtils
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

internal object NetworkModule {
    fun provideRetrofit(
        baseUrl: String,
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()

    fun provideOkHttp(
        isDebug: Boolean,
        interceptors: List<Interceptor>
    ): OkHttpClient {
        val logging = HttpLoggingInterceptor()

        if (isDebug)
            logging.level = HttpLoggingInterceptor.Level.BODY
        else logging.level = HttpLoggingInterceptor.Level.NONE

        val okHttpClientBuilder = OkHttpClient.Builder()
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .connectTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .addInterceptor(LanguageInterceptor())

        interceptors
            .forEach { okHttpClientBuilder.interceptors().add(it) }

        return okHttpClientBuilder.build()
    }

    fun provideGson(): Gson {
        return Gson()
            .newBuilder()
            .create()
    }
}

object KoinModules {
    private const val BASE_URL = "base-url"

    private val moduleList = arrayListOf<Module>()

    private const val SHARED_PREFERENCES_NAME = "shared-preferences-name"
    private const val IS_DEBUG = "is-debug"
    private const val INTERCEPTORS = "interceptors"

    fun initApplicationModule(
        baseUrl: String,
        sharedPreferencesName: String,
        isDebug: Boolean,
        interceptors: List<Interceptor>
    ): ArrayList<Module> {

        // application configuration module
        moduleList.add(module {
            single(named(BASE_URL)) { baseUrl }
            single(named(SHARED_PREFERENCES_NAME)) { sharedPreferencesName }
            single(named(IS_DEBUG)) { isDebug }
            single(named(INTERCEPTORS)) { interceptors }
            single { NetworkModule.provideGson() }
            single<IConnectivityUtils> { ConnectivityUtils(androidContext()) }
        })

        // local data source module
        moduleList.add(module {
            single<SharedPreferencesInterface> {
                SharedPreferencesUtils(
                    get(),
                    androidContext(),
                    get(named(SHARED_PREFERENCES_NAME))
                )
            }
        })

        // remote data source module
        moduleList.add(module {
            single {
                NetworkModule.provideOkHttp(
                    get(named(IS_DEBUG)),
                    get(named(INTERCEPTORS))
                )
            }
            single {
                NetworkModule.provideRetrofit(
                    get(named(BASE_URL)),
                    get(),
                    get()
                )
            }
        })

        return moduleList
    }
}


