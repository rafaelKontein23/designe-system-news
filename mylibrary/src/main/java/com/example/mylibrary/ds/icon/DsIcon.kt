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
    private var iconDescription: String = ""
    private var badgeDescription: String = "notificações"

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
        importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_YES

        attrs?.let {
            context.withStyledAttributes(it, R.styleable.DsIcon, defStyleAttr, 0) {
                badgeCount = getInt(R.styleable.DsIcon_badgeCount, 0)
                iconDescription = getString(R.styleable.DsIcon_iconDescription) ?: ""
                badgeDescription = getString(R.styleable.DsIcon_badgeDescription) ?: "notificações"
            }
        }

        updateContentDescription()
    }

    fun setIcon(drawable: Drawable?) {
        setImageDrawable(drawable)
    }

    /**
     * Define a descrição de acessibilidade do ícone
     * @param description Descrição que será anunciada pelo leitor de tela
     */
    fun setIconDescription(description: String) {
        this.iconDescription = description
        updateContentDescription()
    }

    /**
     * Define a descrição customizada para o badge
     * @param description Descrição no plural (ex: "mensagens não lidas", "itens pendentes")
     */
    fun setBadgeDescription(description: String) {
        this.badgeDescription = description
        updateContentDescription()
    }

    fun setBadgeCount(count: Int) {
        this.badgeCount = max(0, count)
        updateContentDescription()
        invalidate()
    }

    /**
     * Atualiza a descrição de conteúdo para acessibilidade
     * Combina a descrição do ícone com informações do badge
     */
    private fun updateContentDescription() {
        contentDescription = when {
            badgeCount > 0 && iconDescription.isNotEmpty() -> {
                val badgeText = if (badgeCount == 1) {
                    val singular = if (badgeDescription.endsWith("s")) {
                        badgeDescription.dropLast(1)
                    } else {
                        badgeDescription
                    }
                    "$iconDescription, $badgeCount $singular"
                } else {
                    "$iconDescription, $badgeCount $badgeDescription"
                }
                badgeText
            }
            badgeCount > 0 -> {
                if (badgeCount == 1) {
                    val singular = if (badgeDescription.endsWith("s")) {
                        badgeDescription.dropLast(1)
                    } else {
                        badgeDescription
                    }
                    "$badgeCount $singular"
                } else {
                    "$badgeCount $badgeDescription"
                }
            }
            iconDescription.isNotEmpty() -> iconDescription
            else -> "Ícone"
        }
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

            val radius = 22f
            val centerX = width.toFloat() - radius
            val centerY = radius

            canvas.drawCircle(centerX, centerY, radius, badgePaint)

            val textHeight = textPaint.descent() - textPaint.ascent()
            val textOffset = (textHeight / 2) - textPaint.descent()
            canvas.drawText(text, centerX, centerY + textOffset, textPaint)
        }
    }


}
