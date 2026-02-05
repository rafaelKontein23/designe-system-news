package com.example.mylibrary.ds.text

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes
import com.example.mylibrary.R

/**
 * Componente de texto customizado do Design System.
 * Suporta diferentes pesos de fonte (Poppins) e estilos de texto pré-definidos.
 */
class DsText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    /**
     * Peso da fonte Poppins.
     */
    enum class FontWeight {
        REGULAR, SEMI_BOLD, BOLD, MEDIUM
    }

    /**
     * Estilo de texto pré-definido com tamanho e peso específicos.
     */
    enum class TextStyle(val sizeSp: Float, val weight: FontWeight) {
        HEADER(24f, FontWeight.BOLD),
        SUBTITLE(14f, FontWeight.SEMI_BOLD),
        DESCRIPTION(14f, FontWeight.MEDIUM),
        TEXT(12f, FontWeight.REGULAR),

    }

    private var currentFontWeight: FontWeight = FontWeight.REGULAR
    private var currentTextStyle: TextStyle? = null

    init {
        attrs?.let {
            context.withStyledAttributes(it, R.styleable.DsText) {
                try {
                    val fontWeightIndex = getInt(R.styleable.DsText_dsFontWeight, -1)
                    val textStyleIndex = getInt(R.styleable.DsText_dsTextStyle, -1)

                    if (textStyleIndex >= 0 && textStyleIndex < TextStyle.entries.size) {
                        applyTextStyle(TextStyle.entries[textStyleIndex])
                    }

                    if (fontWeightIndex >= 0 && fontWeightIndex < FontWeight.entries.size) {
                        applyFontWeight(FontWeight.entries[fontWeightIndex])
                    }

                } catch (e: Exception) {
                    applyFontWeight(FontWeight.REGULAR)
                }
            }
        }

        if (typeface == null) {
            applyFontWeight(FontWeight.REGULAR)
        }
    }

    /**
     * Define o peso da fonte.
     * @param weight Peso da fonte (REGULAR, SEMI_BOLD, BOLD)
     */
    fun setFontWeight(weight: FontWeight) {
        applyFontWeight(weight)
    }

    /**
     * Define o estilo de texto pré-definido.
     * Isso também define o tamanho e peso da fonte automaticamente.
     * @param style Estilo de texto (HEADER, SUBTITLE, DESCRIPTION)
     */
    fun setTextStyle(style: TextStyle) {
        applyTextStyle(style)
    }

    /**
     * Retorna o peso da fonte atual.
     */
    fun getFontWeight(): FontWeight = currentFontWeight

    /**
     * Retorna o estilo de texto atual (se aplicado).
     */
    fun getTextStyle(): TextStyle? = currentTextStyle

    /**
     * Define o tamanho do texto em SP (Scale-independent Pixels).
     * @param sizeSp Tamanho em SP
     */
    fun setTextSizeSp(sizeSp: Float) {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, sizeSp)
        currentTextStyle = null
    }

    /**
     * Define texto com múltiplas cores.
     * @param parts Lista de pares (texto, cor) onde cada parte terá sua própria cor
     */
    fun setColoredText(vararg parts: Pair<String, Int>) {
        val spannableString = android.text.SpannableStringBuilder()

        parts.forEach { (text, color) ->
            val start = spannableString.length
            spannableString.append(text)
            spannableString.setSpan(
                android.text.style.ForegroundColorSpan(color),
                start,
                spannableString.length,
                android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        text = spannableString
    }


    private fun applyFontWeight(weight: FontWeight) {
        currentFontWeight = weight

        val fontRes = when (weight) {
            FontWeight.REGULAR -> R.font.poppins
            FontWeight.SEMI_BOLD -> R.font.poppins_semibold
            FontWeight.BOLD -> R.font.poppins_bold
            FontWeight.MEDIUM -> R.font.poppins_medium
        }

        typeface = try {
            ResourcesCompat.getFont(context, fontRes)
        } catch (e: Exception) {
            ResourcesCompat.getFont(context, R.font.poppins)
        }
    }

    private fun applyTextStyle(style: TextStyle) {
        currentTextStyle = style
        setTextSize(TypedValue.COMPLEX_UNIT_SP, style.sizeSp)
        applyFontWeight(style.weight)
    }

    companion object {
        /**
         * Cria um DsText programaticamente com estilo pré-definido.
         */
        @JvmStatic
        fun create(context: Context, style: TextStyle): DsText {
            return DsText(context).apply {
                setTextStyle(style)
            }
        }

        /**
         * Cria um DsText programaticamente com peso de fonte específico.
         */
        @JvmStatic
        fun create(context: Context, weight: FontWeight): DsText {
            return DsText(context).apply {
                setFontWeight(weight)
            }
        }
    }
}
