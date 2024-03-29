package com.ahmoneam.basecleanarchitecture.base.data.local

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import java.util.*

class SharedPreferencesUtils constructor(
    override val gson: Gson,
    private val context: Context,
    private val sharedPreferencesName: String
) : SharedPreferencesInterface {

    //private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val mPrefs: SharedPreferences =
        context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)

    @setparam:ApplicationLanguage
    @get:ApplicationLanguage
    override var language: String
        get() {
            val currentLanguage = getString(CURRENT_LANGUAGE)
            return currentLanguage
                ?: if (Locale.getDefault().language.equals(ApplicationLanguage.ARABIC, true))
                    ApplicationLanguage.ARABIC
                else ApplicationLanguage.ENGLISH
        }
        set(value) {
            putString(CURRENT_LANGUAGE, value)
        }

    override fun putString(key: String, value: String?) {
        mPrefs.edit().putString(key, value).apply()
    }

    override fun getString(key: String): String? {
        return mPrefs.getString(key, null)
    }

    override fun putBoolean(key: String, value: Boolean) {
        mPrefs.edit().putBoolean(key, value).apply()
    }

    override fun getBoolean(key: String): Boolean {
        return mPrefs.getBoolean(key, false)
    }

    override fun clearData() {
        mPrefs.edit().clear().apply()
    }

    companion object {
        const val CURRENT_LANGUAGE = "current_language"
    }
}

