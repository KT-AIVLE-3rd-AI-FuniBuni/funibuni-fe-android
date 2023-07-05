package com.aivle.presentation.myprofile.applyDetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.fragment.NavHostFragment
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseActivity
import com.aivle.presentation.databinding.ActivityWasteDisposalApplyDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WasteDisposalApplyDetailActivity : BaseActivity<ActivityWasteDisposalApplyDetailBinding>(R.layout.activity_waste_disposal_apply_detail) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupNavigation()
    }

    private fun setupNavigation() {
        val navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment)
            .navController

        navController.addOnDestinationChangedListener { _, _, args ->
            args?.getString("title")?.let { title ->
                binding.header.title.text = title
            }
        }

        val wasteDisposalApplyId = intent.getIntExtra(WASTE_DISPOSAL_APPLY_ID, -1)
        if (wasteDisposalApplyId != -1) {
            val bundle = bundleOf(WASTE_DISPOSAL_APPLY_ID to wasteDisposalApplyId)
            navController.navigate(R.id.wasteDisposalApplyDetailFragment3, bundle)
        }
    }

    companion object {

        const val WASTE_DISPOSAL_APPLY_ID = "wasteDisposalApplyId"

        fun getIntent(context: Context, wasteDisposalApplyId: Int) = Intent(
            context, WasteDisposalApplyDetailActivity::class.java
        ).apply {
            putExtra(WASTE_DISPOSAL_APPLY_ID, wasteDisposalApplyId)
        }
    }
}