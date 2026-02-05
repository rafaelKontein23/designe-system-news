package com.example.mylibrary.ds.input

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat
import androidx.customview.widget.ExploreByTouchHelper
import com.example.mylibrary.R

/**
 * Campo de entrada customizado que estende [AppCompatEditText] com suporte a diferentes tipos de teclado,
 * máscaras de entrada e toggle de visibilidade de senha com acessibilidade.
 *
 * @param context O contexto da aplicação
 * @param attrs Os atributos XML do componente
 * @param defStyleAttr O estilo padrão aplicado ao componente
 */
class DsInput @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    /**
     * Tipos de teclado suportados pelo componente.
     */
    enum class KeyboardType {
        /** Teclado numérico com máscara para CPF */
        CPF,
        /** Teclado numérico sem máscara */
        NUMBER,
        /** Teclado numérico com máscara para telefone */
        PHONE,
        /** Teclado para e-mail */
        EMAIL,
        /** Teclado para senha com toggle de visibilidade */
        PASSWORD,
        /** Teclado numérico com máscara para data */
        DATE,
        /** Teclado de texto padrão */
        TEXT
    }

    /**
     * Tipos de máscara disponíveis para formatação de entrada.
     */
    enum class MaskType {
        /** Máscara no formato ###.###.###-## */
        CPF,
        /** Máscara no formato (##) #####-#### */
        PHONE,
        /** Máscara no formato ##/##/#### */
        DATE
    }

    private var isVisiblePassword = false
    private var eyeIcon: Drawable? = null
    private var touchHelper: ExploreByTouchHelper? = null

    private var maskWatcher: TextWatcher? = null

    init {
        setBackgroundResource(R.drawable.input_border_radius)
        setTextAppearance(R.style.DsEditText)

        val padding = context.resources.getDimensionPixelSize(R.dimen.padding_7)
        setPadding(padding, paddingTop, padding, paddingBottom)

        attrs?.let {
            val ta = context.obtainStyledAttributes(it, R.styleable.DsInput)
            val keyboardType = ta.getInt(R.styleable.DsInput_inputKeyboardType,KeyboardType.TEXT.ordinal + 1)
            ta.recycle()
            setKeyboardTypeFromXml(keyboardType)
        }
    }

    /**
     * Define o tipo de teclado e aplica configurações correspondentes.
     *
     * @param type O tipo de teclado a ser configurado
     */
    fun setKeyboardType(type: KeyboardType) {
        clearMask()
        clearPasswordIcon()

        when (type) {
            KeyboardType.CPF -> {
                inputType = InputType.TYPE_CLASS_NUMBER
                applyInputMask(MaskType.CPF)
            }

            KeyboardType.NUMBER -> {
                inputType = InputType.TYPE_CLASS_NUMBER
            }

            KeyboardType.PHONE -> {
                inputType = InputType.TYPE_CLASS_NUMBER
                applyInputMask(MaskType.PHONE)
            }

            KeyboardType.EMAIL -> {
                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            }

            KeyboardType.PASSWORD -> {
                inputPassword()
            }

            KeyboardType.DATE -> {
                inputType = InputType.TYPE_CLASS_NUMBER
                applyInputMask(MaskType.DATE)
            }

            KeyboardType.TEXT -> {
                inputType = InputType.TYPE_CLASS_TEXT
            }
        }
    }

    /**
     * Converte o valor do tipo de teclado vindo do XML para o enum [KeyboardType].
     *
     * @param type O valor inteiro do atributo XML
     */
    private fun setKeyboardTypeFromXml(type: Int) {
        val mapped = when (type) {
            1 -> KeyboardType.CPF
            2 -> KeyboardType.NUMBER
            3 -> KeyboardType.PHONE
            4 -> KeyboardType.EMAIL
            5 -> KeyboardType.PASSWORD
            6 -> KeyboardType.DATE
            else -> KeyboardType.TEXT
        }
        setKeyboardType(mapped)
    }

    /**
     * Define a mensagem de erro e atualiza o estado visual do componente.
     *
     * @param error A mensagem de erro a ser exibida, ou null para remover o erro
     * @param icon O ícone de erro, ou null para usar o padrão
     */
    override fun setError(error: CharSequence?, icon: Drawable?) {
        if (error != null) {
            setBackgroundResource(R.drawable.input_border_radius_error)
            super.setError(error, if (eyeIcon != null) null else icon)
        } else {
            setBackgroundResource(R.drawable.input_border_radius)
            super.setError(null, null)
        }
    }

    /**
     * Configura o campo como entrada de senha com toggle de visibilidade.
     */
    private fun inputPassword() {
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        eyeIcon = ContextCompat.getDrawable(context, R.drawable.baseline_visibility_24)
        updatePasswordIcon()

        val paddingRight = context.resources.getDimensionPixelSize(R.dimen.margin_12)
        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)

        setupPasswordAccessibility()
    }

    /**
     * Remove o ícone de visibilidade de senha e recursos de acessibilidade associados.
     */
    private fun clearPasswordIcon() {
        eyeIcon = null
        setCompoundDrawables(null, null, null, null)
        touchHelper = null
        ViewCompat.setAccessibilityDelegate(this, null)
    }

    /**
     * Configura recursos de acessibilidade para o toggle de visibilidade de senha.
     */
    private fun setupPasswordAccessibility() {
        if (eyeIcon == null) return

        touchHelper = object : ExploreByTouchHelper(this) {
            override fun getVirtualViewAt(x: Float, y: Float): Int {
                val drawableEnd = compoundDrawables[2]
                if (drawableEnd != null) {
                    val drawableRight = right - paddingRight
                    val drawableLeft = drawableRight - drawableEnd.bounds.width()
                    if (x >= drawableLeft && x <= drawableRight) return 1
                }
                return INVALID_ID
            }

            override fun getVisibleVirtualViews(mutableList: MutableList<Int>) {
                if (eyeIcon != null) mutableList.add(1)
            }

            override fun onPopulateNodeForVirtualView(virtualViewId: Int, node: AccessibilityNodeInfoCompat) {
                if (virtualViewId != 1 || eyeIcon == null) return

                val drawableEnd = compoundDrawables[2] ?: return
                val drawableRight = right - paddingRight
                val drawableLeft = drawableRight - drawableEnd.bounds.width()
                val drawableTop = paddingTop
                val drawableBottom = height - paddingBottom

                val actionLabel = if (isVisiblePassword) {
                    context.getString(R.string.hide_password)
                } else {
                    context.getString(R.string.show_password)
                }

                node.contentDescription =
                    "${context.getString(R.string.toggle_password_visibility)}. $actionLabel"
                node.addAction(AccessibilityActionCompat.ACTION_CLICK)
                node.setBoundsInParent(Rect(drawableLeft, drawableTop, drawableRight, drawableBottom))
                node.className = ImageButton::class.java.name
            }

            override fun onPerformActionForVirtualView(
                virtualViewId: Int,
                action: Int,
                arguments: android.os.Bundle?
            ): Boolean {
                if (virtualViewId == 1 && action == AccessibilityNodeInfoCompat.ACTION_CLICK) {
                    togglePasswordVisibility()
                    announceForAccessibility(
                        if (isVisiblePassword) context.getString(R.string.password_visible)
                        else context.getString(R.string.password_hidden)
                    )
                    invalidateVirtualView(1)
                    return true
                }
                return false
            }
        }

        ViewCompat.setAccessibilityDelegate(this, touchHelper)
    }

    /**
     * Atualiza o ícone de visibilidade de senha.
     */
    private fun updatePasswordIcon() {
        eyeIcon?.let {
            val size = context.resources.getDimensionPixelSize(R.dimen.size_24)
            it.setBounds(0, 0, size, size)
        }
        compoundDrawablePadding = context.resources.getDimensionPixelSize(R.dimen.margin_12)
        setCompoundDrawables(null, null, eyeIcon, null)
        touchHelper?.invalidateVirtualView(1)
    }

    /**
     * Intercepta eventos de toque para detectar clique no ícone de visibilidade de senha.
     *
     * @param event O evento de toque
     * @return true se o evento foi consumido, false caso contrário
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (eyeIcon != null && event.action == MotionEvent.ACTION_UP) {
            val drawableEnd = compoundDrawables[2]
            if (drawableEnd != null && event.rawX >= (right - drawableEnd.bounds.width() - paddingRight)) {
                togglePasswordVisibility()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    /**
     * Aplica uma máscara de formatação à entrada de texto.
     *
     * @param maskType O tipo de máscara a ser aplicada
     */
    private fun applyInputMask(maskType: MaskType) {
        val mask = when (maskType) {
            MaskType.CPF -> "###.###.###-##"
            MaskType.PHONE -> "(##) #####-####"
            MaskType.DATE -> "##/##/####"
        }

        val watcher = object : TextWatcher {
            var isUpdating = false
            var oldText = ""

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isUpdating) return
                val str = unmask(s?.toString().orEmpty())
                var masked = ""
                var i = 0

                for (char in mask) {
                    if (char != '#' && str.length > oldText.length) {
                        masked += char
                        continue
                    }
                    if (i >= str.length) break
                    masked += str[i]
                    i++
                }

                isUpdating = true
                setText(masked)
                setSelection(masked.length)
                isUpdating = false
                oldText = str
            }
        }

        maskWatcher = watcher
        addTextChangedListener(watcher)
    }

    /**
     * Remove a máscara de formatação aplicada anteriormente.
     */
    private fun clearMask() {
        maskWatcher?.let { removeTextChangedListener(it) }
        maskWatcher = null
    }

    /**
     * Remove caracteres de formatação de uma string, mantendo apenas dígitos.
     *
     * @param s A string a ser processada
     * @return String contendo apenas dígitos
     */
    private fun unmask(s: String): String = s.replace(Regex("[^\\d]"), "")

    /**
     * Alterna entre senha visível e oculta.
     */
    private fun togglePasswordVisibility() {
        isVisiblePassword = !isVisiblePassword

        if (isVisiblePassword) {
            inputType = InputType.TYPE_CLASS_TEXT
            eyeIcon = ContextCompat.getDrawable(context, R.drawable.baseline_visibility_off_24)
        } else {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            eyeIcon = ContextCompat.getDrawable(context, R.drawable.baseline_visibility_24)
        }

        updatePasswordIcon()
        setSelection(text?.length ?: 0)
    }
}