package com.example.mylibrary.ds.icon

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.example.mylibrary.R
import kotlin.compareTo
import kotlin.div
import kotlin.math.max
import kotlin.text.toFloat
import kotlin.toString
import androidx.core.content.withStyledAttributes

class DsIcon @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var badgeCount: Int = 0
    private val badgePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.FILL
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 24f
        textAlign = Paint.Align.CENTER
        isFakeBoldText = true
    }

    init {
        attrs?.let {
            context.withStyledAttributes(it, R.styleable.DsIcon, defStyleAttr, 0) {
                badgeCount = getInt(R.styleable.DsIcon_badgeCount, 0)
            }
        }
    }

    fun setIcon(drawable: Drawable?) {
        setImageDrawable(drawable)
    }

    fun setBadgeCount(count: Int) {
        this.badgeCount = max(0, count)
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (badgeCount > 0) {
            val badgeSize = 44f // raio * 2
            val extraWidth = (badgeSize * 0.5f).toInt()
            val extraHeight = (badgeSize * 0.5f).toInt()

            setMeasuredDimension(
                measuredWidth + extraWidth,
                measuredHeight + extraHeight
            )
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (badgeCount > 0) {
            val text = if (badgeCount > 99) "99+" else badgeCount.toString()

            // Badge dimension and position (top right corner)
            val radius = 22f
            val centerX = width.toFloat() - radius
            val centerY = radius

            // Draw badge background
            canvas.drawCircle(centerX, centerY, radius, badgePaint)

            // Draw text centered in the badge
            val textHeight = textPaint.descent() - textPaint.ascent()
            val textOffset = (textHeight / 2) - textPaint.descent()
            canvas.drawText(text, centerX, centerY + textOffset, textPaint)
        }
    }


}
