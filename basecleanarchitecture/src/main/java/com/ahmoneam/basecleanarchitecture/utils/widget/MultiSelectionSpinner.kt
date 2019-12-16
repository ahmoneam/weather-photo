package com.ahmoneam.basecleanarchitecture.utils.widget

import android.R
import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.OnMultiChoiceClickListener
import android.util.AttributeSet
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import androidx.appcompat.app.AlertDialog.Builder
import androidx.appcompat.widget.AppCompatSpinner
import java.util.*
import kotlin.collections.ArrayList

class MultiSelectionSpinner : AppCompatSpinner, OnMultiChoiceClickListener {
    private var items: ArrayList<Item>? = null
    private var selection: BooleanArray? = null
    private var adapter: ArrayAdapter<Any?>

    constructor(context: Context?) : super(context) {
        adapter = ArrayAdapter<Any?>(
            context!!,
            R.layout.simple_spinner_item
        )
        super.setAdapter(adapter)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        adapter = ArrayAdapter<Any?>(
            context!!,
            R.layout.simple_spinner_item
        )
        super.setAdapter(adapter)
    }

    override fun onClick(dialog: DialogInterface, which: Int, isChecked: Boolean) {
        if (selection != null && which < selection!!.size) {
            selection!![which] = isChecked
            adapter.clear()
            adapter.add(buildSelectedItemString())
        } else {
            throw IllegalArgumentException(
                "Argument 'which' is out of bounds."
            )
        }
    }

    override fun performClick(): Boolean {
        val builder = Builder(context)
        val itemNames = arrayOfNulls<String>(items!!.size)
        for (i in items!!.indices) {
            itemNames[i] = items!![i].name
        }
        builder.setMultiChoiceItems(itemNames, selection, this)
        builder.setPositiveButton(R.string.ok) { _: DialogInterface, _: Int ->
            // pass
        }
        builder.show()
        return true
    }

    override fun setAdapter(adapter: SpinnerAdapter) {
        throw RuntimeException(
            "setAdapter is not supported by MultiSelectSpinner."
        )
    }

    fun setItems(items: ArrayList<Item>?) {
        this.items = items
        selection = BooleanArray(this.items!!.size)
        adapter.clear()
        adapter.add("")
        Arrays.fill(selection, false)
    }

    fun setSelection(selection: ArrayList<Item>) {
        for (i in this.selection!!.indices) {
            this.selection!![i] = false
        }
        for ((_, value) in selection) {
            for (j in items!!.indices) {
                if (items!![j].value == value) {
                    this.selection!![j] = true
                }
            }
        }
        adapter.clear()
        adapter.add(buildSelectedItemString())
    }

    private fun buildSelectedItemString(): String {
        val sb = StringBuilder()
        var foundOne = false
        for (i in items!!.indices) {
            if (selection!![i]) {
                if (foundOne) {
                    sb.append(", ")
                }
                foundOne = true
                sb.append(items!![i].name)
            }
        }
        return sb.toString()
    }

    val selectedItems: ArrayList<Item>
        get() {
            val selectedItems = ArrayList<Item>()
            for (i in items!!.indices) {
                if (selection!![i]) {
                    selectedItems.add(items!![i])
                }
            }
            return selectedItems
        }
}