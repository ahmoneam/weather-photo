package com.ahmoneam.basecleanarchitecture.utils.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ScrollView
import androidx.core.view.postDelayed
import androidx.core.widget.NestedScrollView
import com.ahmoneam.basecleanarchitecture.utils.findParentOfType
import com.ahmoneam.basecleanarchitecture.utils.scrollDownTo
import com.google.android.material.textfield.TextInputLayout


class SmartTextInputLayout : TextInputLayout {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    private val scrollView by lazy(LazyThreadSafetyMode.NONE) {
        findParentOfType<ScrollView>() ?: findParentOfType<NestedScrollView>()
    }

    private fun scrollIfNeeded() {
        // Wait a bit (like 10 frames) for other UI changes to happen
        scrollView?.postDelayed(160) {
            scrollView?.scrollDownTo(this)
        }
    }

    override fun setError(value: CharSequence?) {
        val changed = error != value

        super.setError(value)


        // to hide error text and remove empty state below this view
//        if (childCount > 1) {
//            val view = getChildAt(1)
//            if (view != null) {
//                view.visibility = View.GONE
//            }
//        }

        // work around https://stackoverflow.com/q/34242902/1916449
//        if (value == null) isErrorEnabled = false

        // work around https://stackoverflow.com/q/31047449/1916449
        if (changed) scrollIfNeeded()
    }

    fun showError() {
        error = " "
        // to hide error text and remove empty state below this view
        if (childCount > 1) {
            val view = getChildAt(1)
            if (view != null) {
                view.visibility = View.GONE
            }
        }
    }

    fun hideError() {
        error = null
    }
}
