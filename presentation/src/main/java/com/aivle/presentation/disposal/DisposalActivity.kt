package com.aivle.presentation.disposal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseActivity
import com.aivle.presentation.databinding.ActivityDisposalBinding
import com.aivle.presentation_design.interactive.ui.BottomUpDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DisposalActivity : BaseActivity<ActivityDisposalBinding>(R.layout.activity_disposal) {

    private val viewModel: DisposalViewModel by viewModels()
    private var currentPageTitle = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val imageUri = intent.getStringExtra(IMAGE_URI)!!
        viewModel.wasteImageLocalUri = imageUri

        initView()
    }

    private fun initView() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHostFragment.navController.addOnDestinationChangedListener { _, _, args ->
            currentPageTitle = args?.getString("title") ?: ""
            binding.title.text = currentPageTitle
        }

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        if (currentPageTitle == "품목 고르기") {
            requestFinishDialog()
        } else {
            super.onBackPressed()
        }
    }

    private fun requestFinishDialog() {
        BottomUpDialog.Builder(supportFragmentManager)
            .title("그만하고 나가시겠습니까?")
            .positiveButton("계속하기")
            .negativeButton("안할래요") {
                finish()
            }
            .show()
    }

    companion object {

        private const val IMAGE_URI = "image-uri"

        fun getIntent(context: Context, imageUri: String): Intent =
            Intent(context, DisposalActivity::class.java).apply {
                putExtra(IMAGE_URI, imageUri)
            }
    }
}
