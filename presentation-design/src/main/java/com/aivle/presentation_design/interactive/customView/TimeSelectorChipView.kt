package com.aivle.presentation_design.interactive.customView

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.CompoundButton
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.marginBottom
import com.aivle.presentation_design.R
import com.google.android.material.chip.Chip

private const val TAG = "TimeSelectorChipView"

class TimeSelectorChipView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : HorizontalScrollView(context, attrs, defStyleAttr, defStyleRes), CompoundButton.OnCheckedChangeListener {

    val selectedTime: String
        get() = chips.find { it.isChecked }?.text.toString()

    private val chips: MutableList<Chip>

    init {
        isVerticalScrollBarEnabled = false
        isHorizontalScrollBarEnabled = false
        clipToPadding = false

        val parentLinearLayout = createParentLinearLayout(context)
        chips = mutableListOf()

        listOf(
            "06:00","06:30","07:00","07:30","08:00","08:30","09:00","09:30","10:00","10:30","11:00","11:30","12:00","12:30","13:00","13:30",
            "14:00","14:30","15:00","15:30","16:00","16:30","17:00","17:30","18:00","18:30","19:00","19:30","20:00","20:30","21:00","21:30",
        ).mapIndexed { index, time ->
            createChip(context, attrs, defStyleAttr, time, isChecked = (index == 0))
        }.chunked(2) { twoChips ->
            val childLinearLayout = createChildLinearLayout(context)
            twoChips.forEach { chip ->
                chip.setOnCheckedChangeListener(this)
                chips.add(chip)
                childLinearLayout.addView(chip)
            }
            parentLinearLayout.addView(childLinearLayout)
        }

        addView(parentLinearLayout)
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (isChecked) {
            Log.d(TAG, "onCheckedChanged(): buttonView=$buttonView, isChecked=$isChecked")
            chips.filter { it.isChecked && it != buttonView }
                .forEach { it.isChecked = false }
        }
    }

    private fun createParentLinearLayout(context: Context): LinearLayout =
        LinearLayout(context).also {
            it.layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            it.orientation = LinearLayout.HORIZONTAL
            it.clipToPadding = false
            setPadding(18.dp(), 0, 18.dp(), 0)
        }

    private fun createChildLinearLayout(context: Context): LinearLayout =
        LinearLayout(context).also {
            it.layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            it.orientation = LinearLayout.VERTICAL
        }

    private fun createChip(context: Context, attrs: AttributeSet?, defStyleAttr: Int, time: String, isChecked: Boolean = false): Chip =
        Chip(context, attrs, defStyleAttr).also {
            it.layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, 56.dp()).also { params ->
                params.setMargins(4.dp(), 0, 4.dp(), 0)
            }
            it.isClickable = true
            it.isCheckable = true
            it.isCheckedIconVisible = false

            it.isChecked = isChecked
            it.text = time
            it.textSize = 18f
            it.setChipBackgroundColorResource(R.color.selector_chip_color)
            it.setTextColor(AppCompatResources.getColorStateList(context, R.color.selector_chip_text_color))
        }

    private fun Int.dp(): Int = (this * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
}