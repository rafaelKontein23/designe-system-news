package com.example.mylibrary.ds.card.notification

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.mylibrary.R
import com.example.mylibrary.databinding.DsNotificationCardBinding

/**
 * Card de notificação customizado que exibe título, data/hora e um chip opcional para indicar novas notificações.
 *
 * Este componente estende [ConstraintLayout] e oferece métodos para configurar o conteúdo
 * e a aparência visual do card de notificação.
 *
 * @param context O contexto da aplicação
 * @param attrs Os atributos XML do componente
 * @param defStyleAttr O estilo padrão aplicado ao componente
 */
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

    /**
     * Define o título da notificação.
     *
     * @param title O texto do título a ser exibido
     */
    fun setTitle(title: String) {
        binding.dsNotificationTitle.text = title
    }

    /**
     * Define a data e hora da notificação.
     *
     * @param dateTime O texto da data/hora a ser exibido
     */
    fun setDateTime(dateTime: String) {
        binding.dsNotificationDatetime.text = dateTime
    }

    /**
     * Define se a notificação é nova, controlando a visibilidade do chip.
     *
     * @param isNew true para exibir o chip de nova notificação, false para ocultá-lo
     */
    fun setIsNew(isNew: Boolean) {
        binding.dsNotificationChip.visibility = if (isNew) View.VISIBLE else View.GONE
    }

    /**
     * Define o texto exibido no chip de notificação.
     *
     * @param text O texto a ser exibido no chip
     */
    fun setChipText(text: String) {
        binding.dsNotificationChip.setDsText(text)
    }

    /**
     * Define a cor de fundo do chip de notificação.
     *
     * @param color O recurso de cor a ser aplicado ao fundo do chip
     */
    fun setChipBackgroundColor(color: Int) {
        binding.dsNotificationChip.setDsBackgroundColor(color)
    }

    /**
     * Define a cor do texto do chip de notificação.
     *
     * @param color O recurso de cor a ser aplicado ao texto do chip
     */
    fun setChipTextColor(color: Int) {
        binding.dsNotificationChip.setDsTextColor(color)
    }
}
