package com.example.mylibrary.ds.button

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.example.mylibrary.R
import com.google.android.material.button.MaterialButton

/**
 * Componente de botão customizado que estende [MaterialButton] com suporte a diferentes tipos visuais.
 *
 * Este componente oferece estilos pré-definidos (primário, secundário, outlined, texto e danger)
 * e permite personalização de cores, texto e ícones através de XML ou programaticamente.
 *
 * @param context O contexto da aplicação
 * @param attrs Os atributos XML do componente
 * @param defStyleAttr O estilo padrão aplicado ao componente
 */
class DsButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.materialButtonStyle
) : MaterialButton(context, attrs, defStyleAttr) {

    /**
     * Tipos de botão disponíveis com estilos visuais pré-definidos.
     */
    enum class ButtonType {
        /** Botão primário com fundo azul e texto branco */
        PRIMARY,
        /** Botão secundário com fundo cinza e texto branco */
        SECONDARY,
        /** Botão com borda azul, fundo transparente e texto azul */
        OUTLINED,
        /** Botão sem fundo com texto azul */
        TEXT,
        /** Botão de perigo com fundo vermelho e texto branco */
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

    /**
     * Define o tipo visual do botão.
     *
     * @param type O tipo de botão a ser aplicado
     */
    fun setButtonType(type: ButtonType) {
        currentType = type
        applyButtonType(type)
    }

    /**
     * Aplica as configurações visuais correspondentes ao tipo de botão.
     *
     * @param type O tipo de botão cujas configurações serão aplicadas
     */
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

    /**
     * Define a cor de fundo do botão usando um valor de cor.
     *
     * @param color A cor a ser aplicada ao fundo do botão
     */
    fun setDsBackgroundColor(@ColorInt color: Int) {
        backgroundTintList = android.content.res.ColorStateList.valueOf(color)
    }

    /**
     * Define a cor de fundo do botão usando um recurso de cor.
     *
     * @param colorRes O recurso de cor a ser aplicado ao fundo do botão
     */
    fun setDsBackgroundColorResource(@ColorRes colorRes: Int) {
        backgroundTintList = ContextCompat.getColorStateList(context, colorRes)
    }

    /**
     * Define o texto exibido no botão.
     *
     * @param text O texto a ser exibido
     */
    fun setDsText(text: String) {
        this.text = text
    }

    /**
     * Define um listener de clique para o botão.
     *
     * @param clickListener A função a ser executada quando o botão for clicado
     */
    fun setDsClickListener(clickListener: () -> Unit) {
        setOnClickListener { clickListener() }
    }
}