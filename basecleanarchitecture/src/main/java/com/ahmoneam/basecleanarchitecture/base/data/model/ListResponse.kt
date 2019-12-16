package com.ahmoneam.basecleanarchitecture.base.data.model

import com.google.gson.annotations.SerializedName

data class ListResponse<T>(
    @SerializedName("list") val list: List<T>,
    @SerializedName("metadata") val metadata: ListResponseMetadata
)


