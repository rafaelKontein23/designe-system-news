package com.example.mylibrary.ds.card.notification

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.mylibrary.R
import com.example.mylibrary.databinding.DsNotificationCardBinding

class DsNotificationCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: DsNotificationCardBinding =
        DsNotificationCardBinding.inflate(LayoutInflater.from(context), this, true)

    init {

        isClickable = true
        isFocusable = true
        importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_YES

        val a = context.obtainStyledAttributes(attrs, R.styleable.DsNotificationCard)
        try {
            val title = a.getString(R.styleable.DsNotificationCard_notificationTitle)
            val dateTime = a.getString(R.styleable.DsNotificationCard_notificationDateTime)
            val isNew = a.getBoolean(R.styleable.DsNotificationCard_isNew, false)

            title?.let { setTitle(it) }
            dateTime?.let { setDateTime(it) }
            setIsNew(isNew)
        } finally {
            a.recycle()
        }
    }

    fun setTitle(title: String) {
        binding.dsNotificationTitle.text = title
    }

    fun setDateTime(dateTime: String) {
        binding.dsNotificationDatetime.text = dateTime
    }

    fun setIsNew(isNew: Boolean) {
        binding.dsNotificationChip.visibility = if (isNew) View.VISIBLE else View.GONE
    }

    fun setChipText(text: String) {
        binding.dsNotificationChip.setDsText(text)
    }

    fun setChipBackgroundColor(color: Int) {
        binding.dsNotificationChip.setDsBackgroundColor(color)
    }

    fun setChipTextColor(color: Int) {
        binding.dsNotificationChip.setDsTextColor(color)
    }
}
