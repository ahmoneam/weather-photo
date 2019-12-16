package com.ahmoneam.basecleanarchitecture.base.data.local

import org.koin.core.KoinComponent

interface LocalDataSource : KoinComponent {
    val sharedPreferences: SharedPreferencesInterface
}