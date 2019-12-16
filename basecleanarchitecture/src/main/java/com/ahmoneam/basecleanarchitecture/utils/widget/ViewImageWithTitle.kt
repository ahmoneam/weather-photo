package com.ahmoneam.basecleanarchitecture.utils.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.ahmoneam.basecleanarchitecture.R
import kotlinx.android.synthetic.main.view_image_with_title.view.*

class ViewImageWithTitle : LinearLayout {
    constructor(context: Context) : super(context) {
        initView(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        initView(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes) {
        initView(attrs)
    }

    private fun initView(attrs: AttributeSet?) {
        inflate(context, R.layout.view_image_with_title, this)
        orientation = VERTICAL
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.ViewImageWithTitle)
        imageView.setImageDrawable(attributes.getDrawable(R.styleable.ViewImageWithTitle_image))
        val string = attributes.getString(R.styleable.ViewImageWithTitle_text)
        textView.text = string
        attributes.recycle()
    }
}
