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

    enum class KeyboardType {
        CPF, NUMBER, PHONE, EMAIL, PASSWORD, DATE, TEXT
    }

    enum class MaskType {
        CPF, PHONE, DATE
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

    private fun clearPasswordIcon() {
        eyeIcon = null
        setCompoundDrawables(null, null, null, null)
        touchHelper = null
        ViewCompat.setAccessibilityDelegate(this, null)
    }

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

    private fun updatePasswordIcon() {
        eyeIcon?.let {
            val size = context.resources.getDimensionPixelSize(R.dimen.size_24)
            it.setBounds(0, 0, size, size)
        }
        compoundDrawablePadding = context.resources.getDimensionPixelSize(R.dimen.margin_12)
        setCompoundDrawables(null, null, eyeIcon, null)
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

    private fun clearMask() {
        maskWatcher?.let { removeTextChangedListener(it) }
        maskWatcher = null
    }

    private fun unmask(s: String): String = s.replace(Regex("[^\\d]"), "")

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