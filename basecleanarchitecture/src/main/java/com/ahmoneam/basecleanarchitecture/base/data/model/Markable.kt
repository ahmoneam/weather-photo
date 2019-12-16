package com.ahmoneam.basecleanarchitecture.base.data.model

data class Markable<T>(val data: T, var selectable: Boolean = true, var isSelected: Boolean = false)