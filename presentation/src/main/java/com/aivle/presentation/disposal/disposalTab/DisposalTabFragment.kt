package com.aivle.presentation.disposal.disposalTab

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseFragment
import com.aivle.presentation.databinding.FragmentDisposalV1Binding
import com.aivle.presentation.disposal.DisposalActivity
import com.aivle.presentation.util.common.CameraManager
import com.aivle.presentation.util.ext.showToast
import com.loggi.core_util.extensions.log

class DisposalTabFragment : BaseFragment<FragmentDisposalV1Binding>(R.layout.fragment_disposal_v1) {

    private val cameraManager = CameraManager(this, object : CameraManager.CameraListener {
        override fun onImageCapture(bitmap: Bitmap?, imageUri: String?) {
            if (imageUri != null) {
                log("onImageCapture: $imageUri")
                startDisposalActivity(imageUri)
            } else {
                showToast("다시 시도해주세요")
            }
        }
    })

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            log("PickVisualMedia: uri=$uri")
            log("PickVisualMedia: uri=${Uri.parse(uri.path)}")

            val bitmap = CameraManager.getImageBitmapFrom(requireContext(), uri)
            val file = CameraManager.bitmapToFile(requireContext(), bitmap)

            log("PickVisualMedia: file?.name=${file?.name}")
            log("PickVisualMedia: file?.absolutePath=${file?.absolutePath}")

            startDisposalActivity(file?.absolutePath)
        } else {
            showToast("No media selected")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cameraManager.init()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        binding.btnCamera.setOnClickListener {
            cameraManager.executeCamera()
        }
        binding.btnGallery.setOnClickListener {
            showImagePicker()
        }
    }

    private fun showImagePicker() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startDisposalActivity(imageUri: String?) {
        if (imageUri != null) {
            startActivity(DisposalActivity.getIntent(requireContext(), imageUri))
        }
    }
}