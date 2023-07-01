package com.aivle.presentation_design.interactive.customView

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import com.aivle.presentation_design.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class TimeSelectorChipGroupViewOld @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): ChipGroup(context, attrs, defStyleAttr) {

    val selectedTime: String
        get() = chips.find { it.id == checkedChipId }?.text.toString()

    private val chips: MutableList<Chip>

    init {
        isSingleLine = true
        isSingleSelection = true
        isSelectionRequired = true
        clipToPadding = true

//        chips = listOf(
//            "06:00","06:30","07:00","07:30","08:00","08:30","09:00","09:30","10:00","10:30","11:00","11:30","12:00","12:30","13:00","13:30",
//            "14:00","14:30","15:00","15:30","16:00","16:30","17:00","17:30","18:00","18:30","19:00","19:30","20:00","20:30","21:00","21:30"
//        ).mapIndexed { index, time ->
//            createChip(context, attrs, defStyleAttr, time, isChecked = (index == 0))
//        }.onEach { chip ->
//            addView(chip)
//        }.toMutableList()

        chips = mutableListOf()

        listOf(
            "06:00","06:30","07:00","07:30","08:00","08:30","09:00","09:30","10:00","10:30","11:00","11:30","12:00","12:30","13:00","13:30",
            "14:00","14:30","15:00","15:30","16:00","16:30","17:00","17:30","18:00","18:30","19:00","19:30","20:00","20:30","21:00","21:30"
        ).mapIndexed { index, time ->
            createChip(context, attrs, defStyleAttr, time, isChecked = (index == 0))
        }.chunked(2) { twoChips ->
            val linearLayout = createLinearLayout(context, attrs, defStyleAttr)
            twoChips.forEach { chip ->
                chips.add(chip)
                linearLayout.addView(chip)
            }
            addView(linearLayout)
        }
    }

    private fun createLinearLayout(context: Context, attrs: AttributeSet?, defStyleAttr: Int): LinearLayout =
        LinearLayout(context, attrs, defStyleAttr).also { layout ->
            layout.layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            layout.orientation = LinearLayout.VERTICAL
        }

    private fun createChip(context: Context, attrs: AttributeSet?, defStyleAttr: Int, time: String, isChecked: Boolean = false): Chip =
        Chip(context, attrs, defStyleAttr).also {
            // it.layoutParams = LayoutParams(WRAP_CONTENT, 56.dp())
            it.layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, 56.dp())
            it.isClickable = true
            it.isCheckable = true
            it.isCheckedIconVisible = false

            it.isChecked = isChecked
            it.text = time
            it.textSize = 18f
            it.setChipBackgroundColorResource(R.color.selector_chip_color)
            it.setTextColor(AppCompatResources.getColorStateList(context, R.color.selector_chip_text_color))
        }

    private fun createChipColorStateList(): ColorStateList = ColorStateList(
        arrayOf(
            intArrayOf(android.R.attr.state_checked),  // checked
            intArrayOf(-android.R.attr.state_checked), // unchekced
        ),
        intArrayOf(
            context.getColor(R.color.theme_color),
            context.getColor(R.color.gray200),
        )
    )

    private fun Int.dp(): Int = (this * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
}