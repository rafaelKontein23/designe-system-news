package com.example.mylibrary.ds.input

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.example.mylibrary.R
import com.example.mylibrary.databinding.DsInputTextBinding

class DsInput @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
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

    private val binding = DsInputTextBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        attrs?.let {
            val ta = context.obtainStyledAttributes(it, R.styleable.DsInput)
            val keyboardType = ta.getInt(R.styleable.DsInput_inputKeyboardType, 1)

            binding.inputText.hint = ta.getString(R.styleable.DsInput_android_hint)

            ta.getString(R.styleable.DsInput_android_text)?.let { text ->
                binding.inputText.setText(text)
            }

            val textColor = ta.getColor(R.styleable.DsInput_android_textColor, -1)
            if (textColor != -1) binding.inputText.setTextColor(textColor)

            val textColorHint = ta.getColor(R.styleable.DsInput_android_textColorHint, -1)
            if (textColorHint != -1) binding.inputText.setHintTextColor(textColorHint)

            val inputType = ta.getInt(R.styleable.DsInput_android_inputType, -1)
            if (inputType != -1) binding.inputText.inputType = inputType

            val maxLength = ta.getInt(R.styleable.DsInput_android_maxLength, -1)
            if (maxLength != -1) {
                binding.inputText.filters = arrayOf(android.text.InputFilter.LengthFilter(maxLength))
            }

            ta.recycle()

            when (keyboardType) {
                CPF -> {
                    binding.inputText.inputType = InputType.TYPE_CLASS_NUMBER
                    applyInputMask(binding.inputText, MaskType.CPF)
                }

                NUMBER -> {
                    inputNumber()
                }

                PHONE -> {
                    binding.inputText.inputType = InputType.TYPE_CLASS_NUMBER

                    applyInputMask(binding.inputText, MaskType.PHONE)
                }

                EMAIL -> {
                    inputEmail()
                }

                PASSWORD -> {
                    inputPassword()
                }
                DATE -> {
                    binding.inputText.inputType = InputType.TYPE_CLASS_NUMBER
                    applyInputMask(binding.inputText, MaskType.DATE)
                }

                TEXT -> {
                    binding.inputText.inputType = InputType.TYPE_CLASS_TEXT
                }
                else -> {
                    android.text.InputType.TYPE_CLASS_TEXT
                }
            }

        }
    }

    fun applyInputMask(editText: EditText, maskType: MaskType) {
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
        binding.inputText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        binding.eyePassword.isVisible = false
    }
    fun getTextString(): String = binding.inputText.text?.toString() ?: ""
    fun setText(text:String) {
        binding.inputText.setText(text)
    }

    private fun inputPassword() {
        binding.inputText.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        binding.eyePassword.isVisible = true
        val originalLeft = binding.inputText.paddingLeft
        val originalTop = binding.inputText.paddingTop
        val originalRight = context.resources.getDimensionPixelSize(R.dimen.padding_52)
        val originalBottom = binding.inputText.paddingBottom

        binding.inputText.setPadding(
            originalLeft, originalTop, originalRight, originalBottom
        )

        binding.eyePassword.setOnClickListener {
            if (isVisiblePassword) {
                isVisiblePassword = false
                binding.inputText.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.eyePassword.setImageResource(R.drawable.baseline_visibility_24)
            } else {
                isVisiblePassword = true
                binding.inputText.inputType = InputType.TYPE_CLASS_TEXT
                binding.eyePassword.setImageResource(R.drawable.baseline_visibility_off_24)
            }
        }
    }

    private fun inputNumber() {
        binding.eyePassword.isVisible = false
        binding.inputText.inputType = InputType.TYPE_CLASS_NUMBER
    }

    enum class MaskType {
        CPF, PHONE, DATE
    }
}