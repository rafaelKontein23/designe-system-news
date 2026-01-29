package com.example.mylibrary.ds.button

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.example.mylibrary.R
import com.google.android.material.button.MaterialButton

class DsButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.materialButtonStyle
) : MaterialButton(context, attrs, defStyleAttr) {

    enum class ButtonType {
        PRIMARY,
        SECONDARY,
        OUTLINED,
        TEXT,
        DANGER
    }

    private var currentType: ButtonType = ButtonType.PRIMARY

    init {
        insetTop = 0
        insetBottom = 0
        textAlignment = TEXT_ALIGNMENT_CENTER
        gravity = android.view.Gravity.CENTER
        cornerRadius = resources.getDimensionPixelSize(R.dimen.border_button_radius)

        attrs?.let { attribute ->
            context.obtainStyledAttributes(attribute, R.styleable.DsButton).apply {
                val defaultText = text
                val textDS = getString(R.styleable.DsButton_textDS)
                text = textDS ?: defaultText

                val typeIndex = getInt(R.styleable.DsButton_buttonType, 0)
                currentType = ButtonType.entries[typeIndex]

                val bgColor = getColor(R.styleable.DsButton_backgroundColorDS, -1)
                val txtColor = getColor(R.styleable.DsButton_textColorDS, -1)

                val iconStartRes = getResourceId(R.styleable.DsButton_iconStart, -1)
                val iconEndRes = getResourceId(R.styleable.DsButton_iconEnd, -1)

                recycle()

                applyButtonType(currentType)

                if (bgColor != -1) setDsBackgroundColor(bgColor)
                if (txtColor != -1) setTextColor(txtColor)
                if (iconStartRes != -1) setIconResource(iconStartRes)
                if (iconEndRes != -1) icon = ContextCompat.getDrawable(context, iconEndRes)
            }
        }
    }

    fun setButtonType(type: ButtonType) {
        currentType = type
        applyButtonType(type)
    }

    private fun applyButtonType(type: ButtonType) {
        when (type) {
            ButtonType.PRIMARY -> {
                backgroundTintList = ContextCompat.getColorStateList(context, android.R.color.holo_blue_dark)
                setTextColor(ContextCompat.getColor(context, android.R.color.white))
                strokeWidth = 0
            }
            ButtonType.SECONDARY -> {
                backgroundTintList = ContextCompat.getColorStateList(context, android.R.color.darker_gray)
                setTextColor(ContextCompat.getColor(context, android.R.color.white))
                strokeWidth = 0
            }
            ButtonType.OUTLINED -> {
                backgroundTintList = ContextCompat.getColorStateList(context, android.R.color.transparent)
                setTextColor(ContextCompat.getColor(context, android.R.color.holo_blue_dark))
                strokeWidth = 4
                strokeColor = ContextCompat.getColorStateList(context, android.R.color.holo_blue_dark)
            }
            ButtonType.TEXT -> {
                backgroundTintList = ContextCompat.getColorStateList(context, android.R.color.transparent)
                setTextColor(ContextCompat.getColor(context, android.R.color.holo_blue_dark))
                strokeWidth = 0
                elevation = 0f
            }
            ButtonType.DANGER -> {
                backgroundTintList = ContextCompat.getColorStateList(context, android.R.color.holo_red_dark)
                setTextColor(ContextCompat.getColor(context, android.R.color.white))
                strokeWidth = 0
            }
        }
    }


    fun setDsBackgroundColor(@ColorInt color: Int) {
        backgroundTintList = android.content.res.ColorStateList.valueOf(color)
    }

    fun setDsBackgroundColorResource(@ColorRes colorRes: Int) {
        backgroundTintList = ContextCompat.getColorStateList(context, colorRes)
    }

    fun setDsText(text: String) {
        this.text = text
    }

    fun setDsClickListener(clickListener: () -> Unit) {
        setOnClickListener { clickListener() }
    }
}