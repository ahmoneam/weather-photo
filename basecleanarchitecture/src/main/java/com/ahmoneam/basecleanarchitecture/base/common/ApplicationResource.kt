package com.ahmoneam.basecleanarchitecture.base.common

import android.content.Context
import androidx.annotation.StringRes
import org.koin.core.KoinComponent

class ApplicationResource(private val context: Context) : IApplicationResource, KoinComponent {
    override fun getString(@StringRes id: Int): String {
        return context.getString(id)
    }
}