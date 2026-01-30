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

class DsInput @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    companion object {
        private const val CPF = 1
        private const val NUMBER = 2
        private const val PHONE = 3
        private const val EMAIL = 4
        private const val PASSWORD = 5
        private const val DATE = 6
        private const val TEXT = 7
    }

    private var isVisiblePassword = false
    private var eyeIcon: Drawable? = null
    private var touchHelper: ExploreByTouchHelper? = null

    init {
        setBackgroundResource(R.drawable.input_border_radius)
        setTextAppearance(R.style.DsEditText)

        val padding = context.resources.getDimensionPixelSize(R.dimen.padding_7)
        setPadding(padding, paddingTop, padding, paddingBottom)

        attrs?.let {
            val ta = context.obtainStyledAttributes(it, R.styleable.DsInput)
            val keyboardType = ta.getInt(R.styleable.DsInput_inputKeyboardType, TEXT)

            ta.recycle()

            when (keyboardType) {
                CPF -> {
                    inputType = InputType.TYPE_CLASS_NUMBER
                    applyInputMask(this, MaskType.CPF)
                }
                NUMBER -> inputNumber()
                PHONE -> {
                    inputType = InputType.TYPE_CLASS_NUMBER
                    applyInputMask(this, MaskType.PHONE)
                }
                EMAIL -> inputEmail()
                PASSWORD -> inputPassword()
                DATE -> {
                    inputType = InputType.TYPE_CLASS_NUMBER
                    applyInputMask(this, MaskType.DATE)
                }
                TEXT -> inputType = InputType.TYPE_CLASS_TEXT
            }
        }
    }

    override fun setError(error: CharSequence?, icon: Drawable?) {
        if (error != null) {
            setBackgroundResource(R.drawable.input_border_radius_error)
            super.setError(error, if (eyeIcon != null) null else icon)
        } else {
            setBackgroundResource(R.drawable.input_border_radius)
            super.setError(null, null)
        }
    }

    private fun inputPassword() {
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        eyeIcon = ContextCompat.getDrawable(context, R.drawable.baseline_visibility_24)
        updatePasswordIcon()

        val paddingRight = context.resources.getDimensionPixelSize(R.dimen.margin_12)
        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
        
        setupPasswordAccessibility()
    }
    
    private fun setupPasswordAccessibility() {
        if (eyeIcon == null) return
        
        touchHelper = object : ExploreByTouchHelper(this) {
            override fun getVirtualViewAt(x: Float, y: Float): Int {
                val drawableEnd = compoundDrawables[2]
                if (drawableEnd != null) {
                    val drawableRight = right - paddingRight
                    val drawableLeft = drawableRight - drawableEnd.bounds.width()
                    if (x >= drawableLeft && x <= drawableRight) {
                        return 1 // ID do botão virtual
                    }
                }
                return INVALID_ID
            }

            override fun getVisibleVirtualViews(mutableList: MutableList<Int>) {
                if (eyeIcon != null) {
                    mutableList.add(1) // ID do botão virtual
                }
            }

            override fun onPopulateNodeForVirtualView(
                virtualViewId: Int,
                node: AccessibilityNodeInfoCompat
            ) {
                if (virtualViewId == 1 && eyeIcon != null) {
                    val drawableEnd = compoundDrawables[2]
                    if (drawableEnd != null) {
                        val drawableRight = right - paddingRight
                        val drawableLeft = drawableRight - drawableEnd.bounds.width()
                        val drawableTop = paddingTop
                        val drawableBottom = height - paddingBottom
                        
                        val actionLabel = if (isVisiblePassword) {
                            context.getString(R.string.hide_password)
                        } else {
                            context.getString(R.string.show_password)
                        }
                        
                        node.contentDescription = "${context.getString(R.string.toggle_password_visibility)}. $actionLabel"
                        node.addAction(AccessibilityActionCompat.ACTION_CLICK)
                        node.setBoundsInParent(
                            Rect(
                                drawableLeft,
                                drawableTop,
                                drawableRight,
                                drawableBottom
                            )
                        )
                        node.className = ImageButton::class.java.name
                    }
                }
            }

            override fun onPerformActionForVirtualView(
                virtualViewId: Int,
                action: Int,
                arguments: android.os.Bundle?
            ): Boolean {
                if (virtualViewId == 1 && action == AccessibilityNodeInfoCompat.ACTION_CLICK) {
                    togglePasswordVisibility()
                    announceForAccessibility(
                        if (isVisiblePassword) {
                            context.getString(R.string.password_visible)
                        } else {
                            context.getString(R.string.password_hidden)
                        }
                    )
                    invalidateVirtualView(1)
                    return true
                }
                return false
            }
        }
        
        ViewCompat.setAccessibilityDelegate(this, touchHelper)
    }

    private fun updatePasswordIcon() {
        eyeIcon?.let {
            val size = context.resources.getDimensionPixelSize(R.dimen.size_24)
            it.setBounds(0, 0, size, size)
        }
        compoundDrawablePadding = context.resources.getDimensionPixelSize(R.dimen.margin_12)
        setCompoundDrawables(null, null, eyeIcon, null)
        
        // Invalida o nó virtual de acessibilidade quando o ícone é atualizado
        touchHelper?.invalidateVirtualView(1)
    }

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
    fun applyInputMask(editText: AppCompatEditText, maskType: MaskType) {
        val mask = when (maskType) {
            MaskType.CPF -> "###.###.###-##"
            MaskType.PHONE -> "(##) #####-####"
            MaskType.DATE -> "##/##/####"
        }

        editText.addTextChangedListener(object : TextWatcher {
            var isUpdating = false
            var oldText = ""

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isUpdating) return
                val str = unmask(s.toString())
                var masked = ""
                var i = 0

                for (char in mask) {
                    if (char != '#' && str.length > oldText.length) {
                        masked += char
                        continue
                    }
                    try {
                        masked += str[i]
                    } catch (e: Exception) {
                        break
                    }
                    i++
                }

                isUpdating = true
                editText.setText(masked)
                editText.setSelection(masked.length)
                isUpdating = false
                oldText = str
            }
        })
    }

    private fun unmask(s: String): String {
        return s.replace(Regex("[^\\d]"), "")
    }

    private fun inputEmail() {
        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
    }

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

    private fun inputNumber() {
        inputType = InputType.TYPE_CLASS_NUMBER
    }

    enum class MaskType {
        CPF, PHONE, DATE
    }
}