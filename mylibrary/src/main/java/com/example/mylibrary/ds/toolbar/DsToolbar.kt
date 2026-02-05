package com.example.mylibrary.ds.toolbar

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.core.view.updatePadding
import com.example.mylibrary.R
import com.example.mylibrary.ds.text.DsText
import com.google.android.material.appbar.MaterialToolbar

class DsToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialToolbar(context, attrs, defStyleAttr) {

    private var onBackClickListener: (() -> Unit)? = null
    private var onAction1ClickListener: (() -> Unit)? = null
    private var onAction2ClickListener: (() -> Unit)? = null

    private var titleTextView: DsText? = null
    private var titleStyle: DsText.TextStyle = DsText.TextStyle.HEADER

    private val defaultHorizontalPadding =
        context.resources.getDimensionPixelSize(R.dimen.margin_12)

    init {
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
        elevation = 0f

        setPadding(defaultHorizontalPadding, 0, defaultHorizontalPadding, 0)

        minimumHeight = context.resources.getDimensionPixelSize(R.dimen.toolbar_height)

        setContentInsetsAbsolute(0, 0)
        setContentInsetsRelative(0, 0)
        contentInsetStartWithNavigation = 0
        contentInsetEndWithActions = 0
    }

    /**
     * Define o título da toolbar com estilo personalizado
     * @param titleText Texto do título
     * @param textStyle Estilo do texto (HEADER ou SUBTITLE)
     * @param centered Se true, centraliza o título
     */
    fun setToolbarTitle(
        titleText: String,
        textStyle: DsText.TextStyle = DsText.TextStyle.DESCRIPTION,
        centered: Boolean = false
    ) {
        titleStyle = textStyle

        if (centered) {
            title = ""

            titleTextView?.let { removeView(it) }

            titleTextView = DsText(context).apply {
                text = titleText
                setTextStyle(textStyle)
                gravity = Gravity.CENTER
                layoutParams = LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.MATCH_PARENT
                ).apply {
                    gravity = Gravity.CENTER
                }
            }

            addView(titleTextView)
        } else {
            title = titleText
            setTitleTextAppearance(
                context,
                when (textStyle) {
                    DsText.TextStyle.HEADER -> R.style.TextAppearance_Toolbar_Title_Header
                    DsText.TextStyle.SUBTITLE -> R.style.TextAppearance_Toolbar_Title_Subtitle
                    else -> R.style.TextAppearance_Toolbar_Title_Subtitle
                }
            )
        }
    }


    /**
     * Configura o botão de voltar com ícone de 24x24
     * @param show Se true, exibe o botão de voltar
     * @param onClick Ação ao clicar no botão
     */
    fun setBackButton(show: Boolean, onClick: (() -> Unit)? = null) {
        if (show) {
            updatePadding(left = 0)
            val drawable = ContextCompat.getDrawable(context, R.drawable.ds_icon_chevron_back)
            val scaledDrawable = scaleDrawable(drawable, 24, 24)
            navigationIcon = scaledDrawable
            onBackClickListener = onClick
            setNavigationOnClickListener {
                onBackClickListener?.invoke()
            }
        } else {
            updatePadding(left = defaultHorizontalPadding)
            navigationIcon = null
            setNavigationOnClickListener(null)
        }
    }

    /**
     * Adiciona botões de ação na direita com ícones de 24x24
     * @param action1Icon Ícone do primeiro botão (null para não exibir)
     * @param action1Click Ação do primeiro botão
     * @param action2Icon Ícone do segundo botão (null para não exibir)
     * @param action2Click Ação do segundo botão
     */
    fun setActionButtons(
        action1Icon: Int? = null,
        action1Click: (() -> Unit)? = null,
        action2Icon: Int? = null,
        action2Click: (() -> Unit)? = null
    ) {
        menu.clear()

        action1Icon?.let { icon ->
            val drawable = ContextCompat.getDrawable(context, icon)
            val scaledDrawable = scaleDrawable(drawable, 24, 24)
            val menuItem1 = menu.add(Menu.NONE, 1, Menu.NONE, "")
            menuItem1.icon = scaledDrawable
            menuItem1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
            onAction1ClickListener = action1Click
        }

        action2Icon?.let { icon ->
            val drawable = ContextCompat.getDrawable(context, icon)
            val scaledDrawable = scaleDrawable(drawable, 24, 24)
            val menuItem2 = menu.add(Menu.NONE, 2, Menu.NONE, "")
            menuItem2.icon = scaledDrawable
            menuItem2.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
            onAction2ClickListener = action2Click
        }

        setOnMenuItemClickListener { item ->
            when (item.itemId) {
                1 -> onAction1ClickListener?.invoke()
                2 -> onAction2ClickListener?.invoke()
            }
            true
        }
    }

    private fun scaleDrawable(drawable: Drawable?, widthDp: Int, heightDp: Int): Drawable? {
        drawable ?: return null

        val density = resources.displayMetrics.density
        val widthPx = (widthDp * density).toInt()
        val heightPx = (heightDp * density).toInt()

        drawable.setBounds(0, 0, widthPx, heightPx)
        return drawable
    }
}
