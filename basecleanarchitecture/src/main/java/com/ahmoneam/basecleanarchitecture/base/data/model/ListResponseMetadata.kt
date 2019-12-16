package com.ahmoneam.basecleanarchitecture.base.data.model

import com.google.gson.annotations.SerializedName

data class ListResponseMetadata(
    @SerializedName("isNext") val isNext: Boolean,
    @SerializedName("isPrevious") val isPrevious: Boolean
)