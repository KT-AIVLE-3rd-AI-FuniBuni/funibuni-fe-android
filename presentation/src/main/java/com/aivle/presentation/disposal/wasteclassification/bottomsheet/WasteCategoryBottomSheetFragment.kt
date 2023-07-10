package com.aivle.presentation.disposal.wasteclassification.bottomsheet

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.aivle.domain.model.waste.WasteSpec
import com.aivle.presentation.databinding.BottomSheetWasteCategoryBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

interface OnChildFragmentListener {
    fun onSelected(childNumber: Int, index: Int)
}

class WasteCategoryBottomSheetFragment(
    private val wasteSpecs: List<WasteSpec>,
    private val onSelected: (wasteSpec: WasteSpec) -> Unit,
) : BottomSheetDialogFragment(), OnChildFragmentListener {

    private lateinit var binding: BottomSheetWasteCategoryBinding
    private var secondChildListener: OnLargeCategorySelectedListener? = null
    private var finalChoiceSpec: WasteSpec? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetWasteCategoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            setupRatio(bottomSheetDialog)
        }
        return dialog
    }

    @SuppressLint("RestrictedApi", "VisibleForTests")
    private fun setupRatio(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as View
        val behavior = BottomSheetBehavior.from(bottomSheet)
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = getBottomSheetDialogDefaultHeight(0)
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.skipCollapsed = true
        behavior.isDraggable = false
        behavior.disableShapeAnimations()
    }

    private fun getBottomSheetDialogDefaultHeight(offset: Int): Int {
        return (getWindowHeight() * 0.8f - offset).toInt()
    }

    private fun getWindowHeight(): Int {
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        binding.btnBack.setOnClickListener {
            if (binding.viewPager.currentItem < 1) {
                dismiss()
            } else {
                binding.viewPager.currentItem -= 1
            }
        }
        binding.viewPager.adapter = ViewPagerAdapter(this)
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> binding.title.text = "대분류를 선택해주세요"
                    1 -> binding.title.text = "소분류를 선택해주세요"
                }
            }
        })
    }

    override fun onSelected(childNumber: Int, index: Int) {
        if (childNumber == 1) {
            // 대분류 선택 프래그먼트에서 선택한 대분류 인덱스
            val smallCategorySpecs = wasteSpecs.filter { it.index_large_category == index }

            // 만약 소분류가 하나 밖에 없는 항목이라면 바로 종료
            if (smallCategorySpecs.size == 1) {
                finalChoiceSpec = wasteSpecs.find { it.waste_spec_id == smallCategorySpecs.first().waste_spec_id }
                dismiss()
            } else {
                binding.viewPager.currentItem = 1
                if (secondChildListener == null) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        secondChildListener?.onSelected(smallCategorySpecs)
                    }, 100L)
                } else {
                    secondChildListener?.onSelected(smallCategorySpecs)
                }
            }
        } else {
            // 소분류 선택 프래그먼트에서 선택한 최종 WasteSpec
            finalChoiceSpec = wasteSpecs.find { it.waste_spec_id == index }
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        finalChoiceSpec?.let(onSelected)
    }

    interface OnLargeCategorySelectedListener {
        fun onSelected(wasteSpecs: List<WasteSpec>)
    }

    inner class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            val parentFragment = this@WasteCategoryBottomSheetFragment as OnChildFragmentListener

            return when (position) {
                0 -> WasteCategoryLargeFragment(wasteSpecs, parentFragment)
                1 -> WasteCategorySmallFragment(parentFragment).also {
                    secondChildListener = it
                }
                else -> throw IllegalArgumentException("position=$position")
            }
        }
    }
}