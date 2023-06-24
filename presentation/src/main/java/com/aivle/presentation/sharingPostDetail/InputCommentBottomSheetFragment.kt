package com.aivle.presentation.sharingPostDetail

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aivle.presentation.databinding.BottomSheetInputCommentBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class InputCommentBottomSheetFragment constructor(
    private val listener: (text: String) -> Unit
): BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetInputCommentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetInputCommentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun getTheme(): Int {
        return com.google.android.material.R.style.Theme_MaterialComponents_Light_BottomSheetDialog
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setOnShowListener { setup(it as BottomSheetDialog) }
        }
    }

    private fun setup(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as View
        val behavior = BottomSheetBehavior.from(bottomSheet)
        behavior.isDraggable = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddReply.setOnClickListener {
            val text = binding.edtComment.text.toString()
            if (text.isNotBlank()) {
                listener(text)
            }
            dismiss()
        }

        binding.edtComment.requestFocusFromTouch()
    }
}