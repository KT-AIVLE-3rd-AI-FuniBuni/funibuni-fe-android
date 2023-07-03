package com.aivle.presentation.myprofile.applyList

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseActivity
import com.aivle.presentation.databinding.ActivityWasteDisposalApplyListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WasteDisposalApplyListActivity : BaseActivity<ActivityWasteDisposalApplyListBinding>(R.layout.activity_waste_disposal_apply_list) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
    }

    private fun initView() {
        val navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment)
            .navController

        navController.addOnDestinationChangedListener { _, _, args ->
            binding.header.title.text = args?.getString("title")
        }
        binding.header.btnBack.setOnClickListener {
            val remainingStack = navController.popBackStack()
            if (!remainingStack) {
                finish()
            }
        }
    }

    companion object {

        fun getIntent(context: Context) = Intent(context, WasteDisposalApplyListActivity::class.java)
    }
}