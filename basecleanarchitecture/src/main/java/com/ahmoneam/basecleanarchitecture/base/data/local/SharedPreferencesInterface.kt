package com.ahmoneam.basecleanarchitecture.base.data.local

import com.google.gson.Gson

interface SharedPreferencesInterface {
    @ApplicationLanguage
    var language: String
    val gson: Gson

    fun clearData()
    fun getString(key: String): String?
    fun putString(key: String, value: String?)
    fun getBoolean(key: String): Boolean
    fun putBoolean(key: String, value: Boolean)
}
