package com.ahmoneam.basecleanarchitecture.utils.widget

import android.widget.Spinner
import androidx.databinding.BindingAdapter
import com.ahmoneam.basecleanarchitecture.utils.widget.SpinnerExtensions.setSpinnerEntries
import com.ahmoneam.basecleanarchitecture.utils.widget.SpinnerExtensions.setSpinnerItemSelectedListener
import com.ahmoneam.basecleanarchitecture.utils.widget.SpinnerExtensions.setSpinnerValue

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 * @author chetansachdeva on 09/04/18
 */
object SpinnerBindings {

    @JvmStatic
    @BindingAdapter("entries")
    fun Spinner.setEntries(entries: List<Any>?) {
        setSpinnerEntries(entries)
    }

    @JvmStatic
    @BindingAdapter("onItemSelected")
    fun Spinner.setOnItemSelectedListener(itemSelectedListener: SpinnerExtensions.ItemSelectedListener?) {
        setSpinnerItemSelectedListener(itemSelectedListener)
    }

    @JvmStatic
    @BindingAdapter("newValue")
    fun Spinner.setNewValue(newValue: Any?) {
        setSpinnerValue(newValue)
    }
}