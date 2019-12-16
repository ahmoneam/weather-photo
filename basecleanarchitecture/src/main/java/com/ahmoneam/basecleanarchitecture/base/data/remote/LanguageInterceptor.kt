package com.ahmoneam.basecleanarchitecture.base.data.remote

import com.ahmoneam.basecleanarchitecture.base.data.remote.APIS.HEADERS.KEY_ACCEPT_LANGUAGE
import okhttp3.Interceptor
import okhttp3.Response
import java.util.*

class LanguageInterceptor : Interceptor {
    companion object {
        const val LANGUAGE_ENGLISH = "en-US"
        const val LANGUAGE_ARABIC = "ar-SA"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val newBuilder = chain.request().newBuilder()

        val language =
            if (Locale.getDefault().language == "ar") LANGUAGE_ARABIC
            else LANGUAGE_ENGLISH

        newBuilder.addHeader(KEY_ACCEPT_LANGUAGE, language)

        return chain.proceed(newBuilder.build())
    }
}