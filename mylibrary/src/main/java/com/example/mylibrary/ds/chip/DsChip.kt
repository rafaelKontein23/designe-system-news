package com.example.mylibrary.ds.chip

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.mylibrary.R

class DsChip @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val textView: TextView
    private val shapeDrawable = GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.ds_chip, this, true)
        textView = findViewById(R.id.ds_chip_text)
        val root = findViewById<ConstraintLayout>(R.id.ds_chip_root)
        root.background = shapeDrawable

        val defaultBg = ContextCompat.getColor(context, android.R.color.holo_blue_dark)
        val defaultTextColor = ContextCompat.getColor(context, android.R.color.white)

        background = shapeDrawable
        isClickable = true
        isFocusable = true

        val a = context.obtainStyledAttributes(attrs, R.styleable.DsChip)
        try {
            val chipText = a.getString(R.styleable.DsChip_chipText)
            val bgColor = a.getColor(R.styleable.DsChip_chipBackgroundColorDS, defaultBg)
            val txtColor = a.getColor(R.styleable.DsChip_chipTextColorDS, defaultTextColor)

            setDsText(chipText ?: textView.text?.toString().orEmpty())
            setDsBackgroundColor(bgColor)
            setDsTextColor(txtColor)
        } finally {
            a.recycle()
        }

        contentDescription = textView.text
        importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_YES
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        if (h > 0) {
            shapeDrawable.cornerRadius = h / 2f
        }
    }

    fun setDsText(text: String) {
        textView.text = text
        contentDescription = text
    }

    fun setDsBackgroundColor(color: Int) {
        shapeDrawable.setColor(color)
        invalidate()
    }

    fun setDsTextColor(color: Int) {
        textView.setTextColor(color)
    }
}