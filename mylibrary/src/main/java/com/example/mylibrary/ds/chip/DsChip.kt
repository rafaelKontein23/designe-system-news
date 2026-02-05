package com.example.mylibrary.ds.chip

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.mylibrary.R

/**
 * Componente de chip customizado que exibe texto com bordas arredondadas e cores personalizáveis.
 *
 * Este componente estende [ConstraintLayout] e oferece métodos para configurar o texto,
 * cor de fundo e cor do texto do chip. As bordas são automaticamente arredondadas
 * com base na altura do componente.
 *
 * @param context O contexto da aplicação
 * @param attrs Os atributos XML do componente
 * @param defStyleAttr O estilo padrão aplicado ao componente
 */
class DsChip @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val textView: TextView

    /**
     * Drawable de forma usado para desenhar o fundo arredondado do chip.
     */
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

    /**
     * Atualiza o raio das bordas arredondadas quando o tamanho do componente muda.
     * O raio é calculado como metade da altura para criar bordas totalmente arredondadas.
     *
     * @param w A nova largura do componente
     * @param h A nova altura do componente
     * @param oldw A largura anterior do componente
     * @param oldh A altura anterior do componente
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        if (h > 0) {
            shapeDrawable.cornerRadius = h / 2f
        }
    }

    /**
     * Define o texto exibido no chip e atualiza a descrição de acessibilidade.
     *
     * @param text O texto a ser exibido
     */
    fun setDsText(text: String) {
        textView.text = text
        contentDescription = text
    }

    /**
     * Define a cor de fundo do chip.
     *
     * @param color A cor a ser aplicada ao fundo
     */
    fun setDsBackgroundColor(color: Int) {
        shapeDrawable.setColor(color)
        invalidate()
    }

    /**
     * Define a cor do texto do chip.
     *
     * @param color A cor a ser aplicada ao texto
     */
    fun setDsTextColor(color: Int) {
        textView.setTextColor(color)
    }
}