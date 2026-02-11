package com.example.mylibrary.ds.card.news

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.withStyledAttributes
import com.example.mylibrary.R
import com.example.mylibrary.databinding.DsNewsCardBinding
import com.google.android.material.card.MaterialCardView
import kotlin.text.toInt
import androidx.core.graphics.toColorInt

/**
 * Componente de Card de Notícias que utiliza DsText e View Binding.
 * Implementa acessibilidade agrupando as informações no contentDescription do card.
 */
class DsNewsCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val binding: DsNewsCardBinding = DsNewsCardBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    init {
        isClickable = true
        isFocusable = true
        importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_YES
        elevation = 0f
        radius = 12f * resources.displayMetrics.density
        setCardBackgroundColor(context.getColor(android.R.color.white))
        strokeColor = "#E5E7EB".toColorInt()
        strokeWidth = (1f * resources.displayMetrics.density).toInt()




        attrs?.let {
            context.withStyledAttributes(it, R.styleable.DsNewsCard) {
                val title = getString(R.styleable.DsNewsCard_newsTitle)
                val description = getString(R.styleable.DsNewsCard_newsDescription)
                val time = getString(R.styleable.DsNewsCard_newsTime)
                val imageRes = getResourceId(R.styleable.DsNewsCard_newsImage, -1)

                setNews(
                    title = title ?: "",
                    description = description ?: "",
                    time = time ?: ""
                )

                if (imageRes != -1) {
                    binding.newsImage.setImageResource(imageRes)
                }
            }
        }
    }

    /**
     * Preenche os dados da notícia e atualiza a acessibilidade.
     */
    fun setNews(
        title: String,
        description: String,
        time: String,
        imageLoader: (com.google.android.material.imageview.ShapeableImageView.() -> Unit)? = null
    ) {
        binding.newsTitle.text = title
        binding.newsDescription.text = description
        binding.newsTime.text = time

        imageLoader?.invoke(binding.newsImage)

        contentDescription = "Notícia: $title. $description. Postado em $time"
    }

}
