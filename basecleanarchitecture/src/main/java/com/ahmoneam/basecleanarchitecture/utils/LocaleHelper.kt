package com.ahmoneam.basecleanarchitecture.utils

import android.content.Context
import android.os.Build
import com.ahmoneam.basecleanarchitecture.base.data.local.SharedPreferencesInterface
import java.util.*

object LocaleHelper {
    //    val sharedPreferences by getKoinInstance<SharedPreferencesInterface>()
    private val sharedPreferences by getKoinInstance<SharedPreferencesInterface>()

    fun onAttach(context: Context): Context {
        return setLocale(context, sharedPreferences.language)
    }

    fun onAttach(context: Context, language: String): Context {
        return setLocale(context, language)
    }

    private fun setLocale(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, locale)
        } else {
            updateResourcesLegacy(context, locale)
        }
    }

    private fun updateResources(context: Context, locale: Locale): Context {
        Locale.setDefault(locale)
        val resources = context.resources
        val configuration = resources.configuration
        context.createConfigurationContext(configuration)
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)
        Locale.setDefault(locale)
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        return context.createConfigurationContext(configuration)
    }

    private fun updateResourcesLegacy(context: Context, locale: Locale): Context {
        val resources = context.resources
        val configuration = resources.configuration
        configuration.locale = locale
        configuration.setLayoutDirection(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }
}
