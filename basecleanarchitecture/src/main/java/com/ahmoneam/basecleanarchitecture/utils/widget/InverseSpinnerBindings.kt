package com.ahmoneam.basecleanarchitecture.utils.widget

import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.ahmoneam.basecleanarchitecture.utils.widget.SpinnerExtensions.getSpinnerValue
import com.ahmoneam.basecleanarchitecture.utils.widget.SpinnerExtensions.setSpinnerInverseBindingListener
import com.ahmoneam.basecleanarchitecture.utils.widget.SpinnerExtensions.setSpinnerValue

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 * @author chetansachdeva on 10/04/18
 */
object InverseSpinnerBindings {
    @JvmStatic
    @BindingAdapter("selectedValue")
    fun Spinner.setSelectedValue(selectedValue: Any?) = setSpinnerValue(selectedValue)

    @JvmStatic
    @BindingAdapter("selectedValueAttrChanged")
    fun Spinner.setInverseBindingListener(inverseBindingListener: InverseBindingListener?) =
        setSpinnerInverseBindingListener(inverseBindingListener)

    @JvmStatic
    @InverseBindingAdapter(attribute = "selectedValue", event = "selectedValueAttrChanged")
    fun Spinner.getSelectedValue(): Any? = getSpinnerValue()
}