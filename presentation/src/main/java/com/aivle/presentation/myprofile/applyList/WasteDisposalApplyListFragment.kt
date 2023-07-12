package com.aivle.presentation.myprofile.applyList

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aivle.domain.model.waste.WasteDisposalApplyItem
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseFragment
import com.aivle.presentation.databinding.FragmentWasteDisposalApplyListBinding
import com.aivle.presentation.util.ext.repeatOnStarted
import com.aivle.presentation.util.ext.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WasteDisposalApplyListFragment : BaseFragment<FragmentWasteDisposalApplyListBinding>(R.layout.fragment_waste_disposal_apply_list) {

    private val viewModel: WasteDisposalApplyListViewModel by viewModels()

    private lateinit var listAdapter: WasteDisposalApplyListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        observeUiState()

        viewModel.loadWasteDisposalApplies()
    }

    private fun initView() {
        binding.noContentView.container.isVisible = false
        binding.noContentView.message.text = "배출 신청 목록이 없어요"

        listAdapter = WasteDisposalApplyListAdapter()
        binding.applyList.adapter = listAdapter
    }

    private fun observeUiState() = repeatOnStarted {
        viewModel.uiStateFlow.collect { (isLoading, message, data) ->
            message?.let(::showToast)
            showApplyItems(data)
        }
    }

    private fun showApplyItems(applyItems: List<WasteDisposalApplyItem>) {
        binding.noContentView.container.isVisible = applyItems.isEmpty()
        binding.applyList.isVisible = applyItems.isNotEmpty()
        applyItems.forEach {
            it.onClick = ::moveApplyDetail
        }
        listAdapter.submitList(applyItems)
    }

    private fun moveApplyDetail(applyId: Int) {
        val action = WasteDisposalApplyListFragmentDirections
            .actionWasteDisposalApplyListFragmentToWasteDisposalApplyDetailFragment2(applyId)
        findNavController().navigate(action)
    }
}