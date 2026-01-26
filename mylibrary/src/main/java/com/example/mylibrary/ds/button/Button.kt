package com.example.mylibrary.ds.button

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.mylibrary.R
import com.example.mylibrary.databinding.DsButtonBinding


class DsButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = DsButtonBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.DsButton)
            val textDS = typedArray.getString(R.styleable.DsButton_textDS)
            binding.button.text = textDS ?: ""
            typedArray.recycle()
        }
    }

    fun setDsBackgroundColor(@ColorInt color: Int) {
        binding.root.setBackgroundColor(color)
    }
    fun setDsText(text: String) {
        binding.button.text = text
    }
    fun setDsClickListener(clickListener: () -> Unit) {
        binding.button.setOnClickListener { clickListener() }
    }
}