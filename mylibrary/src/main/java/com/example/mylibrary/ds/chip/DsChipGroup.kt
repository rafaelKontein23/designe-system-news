package com.example.mylibrary.ds.chip

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.mylibrary.R
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.JustifyContent

/**
 * Componente de grupo de chips que organiza múltiplos [DsChip] em um layout flexível.
 *
 * Este componente estende [FlexboxLayout] e permite adicionar, remover e gerenciar chips
 * dinamicamente com seleção única (comportamento tipo radio button).
 *
 * Características:
 * - Layout flexível que ajusta automaticamente os chips em múltiplas linhas
 * - Seleção única - apenas um chip pode ser selecionado por vez
 * - Espaçamento configurável entre chips
 * - Callbacks para eventos de seleção
 *
 * @param context O contexto da aplicação
 * @param attrs Os atributos XML do componente
 * @param defStyleAttr O estilo padrão aplicado ao componente
 */
class DsChipGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FlexboxLayout(context, attrs, defStyleAttr) {

    /**
     * Interface de callback para eventos de seleção de chips.
     */
    interface OnChipSelectionListener {
        /**
         * Chamado quando um chip é selecionado ou desmarcado.
         *
         * @param chip O chip que teve sua seleção alterada
         * @param position A posição do chip no grupo
         * @param isSelected Se o chip está selecionado
         */
        fun onChipSelected(chip: DsChip, position: Int, isSelected: Boolean)
    }

    private var chipSpacing: Int = 0
    private var selectedChip: DsChip? = null
    private var selectionListener: OnChipSelectionListener? = null

    private var selectedBackgroundColor: Int = 0
    private var unselectedBackgroundColor: Int = 0
    private var selectedTextColor: Int = 0
    private var unselectedTextColor: Int = 0

    init {
        // Configurar FlexboxLayout para quebra automática de linha
        flexDirection = FlexDirection.ROW
        flexWrap = FlexWrap.WRAP
        justifyContent = JustifyContent.FLEX_START
        alignItems = AlignItems.FLEX_START

        val a = context.obtainStyledAttributes(attrs, R.styleable.DsChipGroup)
        try {

            chipSpacing = a.getDimensionPixelSize(
                R.styleable.DsChipGroup_chipSpacing,
                context.resources.getDimensionPixelSize(R.dimen.chip_spacing_default)
            )

            selectedBackgroundColor = a.getColor(
                R.styleable.DsChipGroup_selectedBackgroundColor,
                ContextCompat.getColor(context, android.R.color.holo_blue_dark)
            )

            unselectedBackgroundColor = a.getColor(
                R.styleable.DsChipGroup_unselectedBackgroundColor,
                ContextCompat.getColor(context, android.R.color.darker_gray)
            )

            selectedTextColor = a.getColor(
                R.styleable.DsChipGroup_selectedTextColor,
                ContextCompat.getColor(context, android.R.color.white)
            )

            unselectedTextColor = a.getColor(
                R.styleable.DsChipGroup_unselectedTextColor,
                ContextCompat.getColor(context, android.R.color.white)
            )
        } finally {
            a.recycle()
        }

        importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_YES
    }

    /**
     * Adiciona um chip ao grupo.
     *
     * @param chip O chip a ser adicionado
     */
    fun addChip(chip: DsChip) {
        val layoutParams = LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(chipSpacing / 2, chipSpacing / 2, chipSpacing / 2, chipSpacing / 2)
        }

        chip.layoutParams = layoutParams

        // Aplicar cores iniciais
        chip.setDsBackgroundColor(unselectedBackgroundColor)
        chip.setDsTextColor(unselectedTextColor)

        // Sempre adicionar listener de clique para seleção única
        chip.setOnClickListener {
            handleChipClick(chip)
        }

        addView(chip)
    }

    /**
     * Adiciona múltiplos chips ao grupo.
     *
     * @param chips Lista de textos para criar os chips
     */
    fun addChips(chips: List<String>) {
        chips.forEach { text ->
            val chip = DsChip(context).apply {
                setDsText(text)
            }
            addChip(chip)
        }
    }

    /**
     * Remove um chip do grupo.
     *
     * @param chip O chip a ser removido
     */
    fun removeChip(chip: DsChip) {
        if (selectedChip == chip) {
            selectedChip = null
        }
        removeView(chip)
    }

    /**
     * Remove todos os chips do grupo.
     */
    fun removeAllChips() {
        selectedChip = null
        removeAllViews()
    }

    /**
     * Trata o clique em um chip com seleção única.
     *
     * @param chip O chip clicado
     */
    private fun handleChipClick(chip: DsChip) {
        // Desmarcar o chip anteriormente selecionado
        selectedChip?.let { previousChip ->
            if (previousChip != chip) {
                updateChipAppearance(previousChip, false)
                val previousPosition = indexOfChild(previousChip)
                selectionListener?.onChipSelected(previousChip, previousPosition, false)
            }
        }

        // Selecionar o chip clicado
        selectedChip = chip
        updateChipAppearance(chip, true)
        notifySelectionChanged(chip, true)
    }

    /**
     * Atualiza a aparência visual do chip com base no estado de seleção.
     *
     * @param chip O chip a ser atualizado
     * @param isSelected Se o chip está selecionado
     */
    private fun updateChipAppearance(chip: DsChip, isSelected: Boolean) {
        if (isSelected) {
            chip.setDsBackgroundColor(selectedBackgroundColor)
            chip.setDsTextColor(selectedTextColor)
        } else {
            chip.setDsBackgroundColor(unselectedBackgroundColor)
            chip.setDsTextColor(unselectedTextColor)
        }
    }

    /**
     * Notifica o listener sobre mudança na seleção.
     *
     * @param chip O chip que teve seleção alterada
     * @param isSelected Se o chip está selecionado
     */
    private fun notifySelectionChanged(chip: DsChip, isSelected: Boolean) {
        val position = indexOfChild(chip)
        selectionListener?.onChipSelected(chip, position, isSelected)
    }

    /**
     * Define o listener para eventos de seleção.
     *
     * @param listener O listener a ser notificado
     */
    fun setOnChipSelectionListener(listener: OnChipSelectionListener) {
        selectionListener = listener
    }

    /**
     * Retorna o chip selecionado.
     *
     * @return O chip selecionado ou null se nenhum estiver selecionado
     */
    fun getSelectedChip(): DsChip? {
        return selectedChip
    }

    /**
     * Retorna o índice do chip selecionado.
     *
     * @return O índice do chip selecionado ou -1 se nenhum estiver selecionado
     */
    fun getSelectedChipIndex(): Int {
        return selectedChip?.let { indexOfChild(it) } ?: -1
    }

    /**
     * Limpa a seleção.
     */
    fun clearSelection() {
        selectedChip?.let { chip ->
            updateChipAppearance(chip, false)
        }
        selectedChip = null
    }

    /**
     * Seleciona um chip programaticamente.
     *
     * @param position A posição do chip a ser selecionado
     */
    fun selectChip(position: Int) {
        if (position >= 0 && position < childCount) {
            val chip = getChildAt(position) as? DsChip
            chip?.let { handleChipClick(it) }
        }
    }

    /**
     * Define o espaçamento entre os chips.
     *
     * @param spacing O espaçamento em pixels
     */
    fun setChipSpacing(spacing: Int) {
        chipSpacing = spacing
        // Atualizar margins de todos os chips existentes
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            (child.layoutParams as? LayoutParams)?.apply {
                setMargins(chipSpacing / 2, chipSpacing / 2, chipSpacing / 2, chipSpacing / 2)
            }
        }
        requestLayout()
    }

    /**
     * Define as cores para os estados selecionado e não selecionado.
     *
     * @param selectedBg Cor de fundo do chip selecionado
     * @param selectedText Cor do texto do chip selecionado
     * @param unselectedBg Cor de fundo do chip não selecionado
     * @param unselectedText Cor do texto do chip não selecionado
     */
    fun setChipColors(
        selectedBg: Int,
        selectedText: Int,
        unselectedBg: Int,
        unselectedText: Int
    ) {
        selectedBackgroundColor = selectedBg
        selectedTextColor = selectedText
        unselectedBackgroundColor = unselectedBg
        unselectedTextColor = unselectedText

        // Atualizar aparência de todos os chips
        for (i in 0 until childCount) {
            val chip = getChildAt(i) as? DsChip
            chip?.let {
                val isSelected = (selectedChip == it)
                updateChipAppearance(it, isSelected)
            }
        }
    }

    /**
     * Retorna o chip em uma posição específica.
     *
     * @param position A posição do chip
     * @return O chip na posição especificada ou null
     */
    fun getChipAt(position: Int): DsChip? {
        return if (position >= 0 && position < childCount) {
            getChildAt(position) as? DsChip
        } else {
            null
        }
    }

    /**
     * Retorna o número de chips no grupo.
     *
     * @return O número de chips
     */
    fun getChipCount(): Int {
        return childCount
    }
}
