package com.ahmoneam.basecleanarchitecture.base.data.model

data class ListItem<T>(
    val item: T,
    val isHeader: Boolean = false,
    val isFooter: Boolean = false,
    val headerStringRes: Int? = null,
    val headerString: String? = null
)