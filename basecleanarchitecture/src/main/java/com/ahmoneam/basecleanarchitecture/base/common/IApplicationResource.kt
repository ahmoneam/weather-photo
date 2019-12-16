package com.ahmoneam.basecleanarchitecture.base.common

import androidx.annotation.StringRes

interface IApplicationResource {
    fun getString(@StringRes id: Int): String
}