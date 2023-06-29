package com.aivle.presentation.disposal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.aivle.presentation.R
import com.aivle.presentation._base.BaseActivity
import com.aivle.presentation.databinding.ActivityDisposalBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DisposalActivity : BaseActivity<ActivityDisposalBinding>(R.layout.activity_disposal) {

    private val viewModel: DisposalViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val imageUri = intent.getStringExtra(IMAGE_URI)!!
        viewModel.wasteImageUri = imageUri
    }

    companion object {

        private const val IMAGE_URI = "image-uri"

        fun getIntent(context: Context, imageUri: String): Intent =
            Intent(context, DisposalActivity::class.java).apply {
                putExtra(IMAGE_URI, imageUri)
            }
    }
}
