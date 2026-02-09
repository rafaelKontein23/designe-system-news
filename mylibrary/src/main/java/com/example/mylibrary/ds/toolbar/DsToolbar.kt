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
    private var onMenuClickListener: (() -> Unit)? = null
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
     * Configura o menu hamburguer na esquerda.
     * @param onClick Ação ao clicar no ícone do menu
     */
    fun setHamburgerMenu(onClick: () -> Unit) {
        updatePadding(left = 0)
        val drawable = ContextCompat.getDrawable(context, R.drawable.ds_icon_menu)
        navigationIcon = scaleDrawable(drawable, 24, 24)
        onMenuClickListener = onClick
        setNavigationOnClickListener {
            onMenuClickListener?.invoke()
        }
    }

    /**
     * Configura o menu popup que abre ao clicar no hambúrguer à esquerda
     * @param menuRes ID do recurso de menu (ex: R.menu.toolbar_menu)
     * @param onMenuItemClick Callback para tratar cliques nos itens
     */
    fun setOptionsMenu(menuRes: Int, onMenuItemClick: (MenuItem) -> Boolean) {
        // Adiciona o ícone hambúrguer à esquerda
        updatePadding(left = 0)
        val drawable = ContextCompat.getDrawable(context, R.drawable.ds_icon_menu)
        navigationIcon = scaleDrawable(drawable, 24, 24)

        setNavigationOnClickListener { view ->
            val popup = androidx.appcompat.widget.PopupMenu(context, view)
            popup.menuInflater.inflate(menuRes, popup.menu)

            try {
                val fieldPopup = popup.javaClass.getDeclaredField("mPopup")
                fieldPopup.isAccessible = true
                val mPopup = fieldPopup.get(popup)

                mPopup.javaClass
                    .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                    .invoke(mPopup, true)

                // Habilita divisores entre
                val menuPopup = mPopup.javaClass
                val setGroupDividerEnabledMethod = menuPopup.getDeclaredMethod("setGroupDividerEnabled", Boolean::class.java)
                setGroupDividerEnabledMethod.invoke(mPopup, true)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            // Define o listener de cliques nos itens
            popup.setOnMenuItemClickListener { menuItem ->
                onMenuItemClick(menuItem)
            }

            popup.show()
        }
    }

    /**
     * Configura o botão de voltar com ícone de 24x24
     */
    fun setBackButton(show: Boolean, onClick: (() -> Unit)? = null) {
        if (show) {
            updatePadding(left = 0)
            val drawable = ContextCompat.getDrawable(context, R.drawable.ds_icon_chevron_back)
            navigationIcon = scaleDrawable(drawable, 24, 24)
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
     * Adiciona botões de ação na direita
     */
    fun setActionButtons(
        action1Icon: Int? = null,
        action1Click: (() -> Unit)? = null,
        action1BadgeCount: Int = 0,
        action1Content: String = "Botão",
        action1BadgeDescription: String = "notificações",
        action2Icon: Int? = null,
        action2Click: (() -> Unit)? = null,
        action2BadgeCount: Int = 0,
        action2Content: String = "Botão",
        action2BadgeDescription: String = "notificações",
    ) {
        menu.clear()

        action1Icon?.let { icon ->
            val menuItem1 = menu.add(Menu.NONE, 1, Menu.NONE, "")
            menuItem1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
            menuItem1.actionView = createActionView(icon, action1BadgeCount, action1Content, action1BadgeDescription)
            menuItem1.actionView?.setOnClickListener { action1Click?.invoke() }
            onAction1ClickListener = action1Click
        }

        action2Icon?.let { icon ->
            val menuItem2 = menu.add(Menu.NONE, 2, Menu.NONE, "")
            menuItem2.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
            menuItem2.actionView = createActionView(icon, action2BadgeCount, action2Content, action2BadgeDescription)
            menuItem2.actionView?.setOnClickListener { action2Click?.invoke() }
            onAction2ClickListener = action2Click
        }
    }

    private fun createActionView(iconRes: Int, badgeCount: Int, contentDesc: String, badgeDesc: String): android.view.View {
        val view = android.view.LayoutInflater.from(context)
            .inflate(R.layout.toolbar_action_with_badge, null)

        val iconView = view.findViewById<android.widget.ImageView>(R.id.action_icon)
        val badgeView = view.findViewById<android.widget.TextView>(R.id.badge)

        iconView.setImageResource(iconRes)

        val description = buildString {
            append(contentDesc)
            if (badgeCount > 0) {
                append(". ")
                append(badgeCount)
                append(" $badgeDesc")
            }
        }

        view.contentDescription = description

        if (badgeCount > 0) {
            badgeView.visibility = android.view.View.VISIBLE
            badgeView.text = if (badgeCount > 99) "99+" else badgeCount.toString()
            badgeView.importantForAccessibility = android.view.View.IMPORTANT_FOR_ACCESSIBILITY_NO
        } else {
            badgeView.visibility = android.view.View.GONE
        }

        return view
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
