package com.ahmoneam.basecleanarchitecture.base.data.model

import com.google.gson.annotations.SerializedName

data class ResponseError(
    @SerializedName("code") val code: Int = -1,
    @SerializedName("message") val message: String = ""
)