package com.aivle.presentation.myprofile.applyList

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
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
        handleViewModelEvent()

        viewModel.loadWasteDisposalApplies()
    }

    private fun initView() {
        listAdapter = WasteDisposalApplyListAdapter()
        binding.applyList.adapter = listAdapter
    }

    private fun handleViewModelEvent() = repeatOnStarted {
        viewModel.eventFlow.collect { event -> when (event) {
            is WasteDisposalApplyListViewModel.Event.None -> {
            }
            is WasteDisposalApplyListViewModel.Event.Failure -> {
                showToast(event.message)
            }
            is WasteDisposalApplyListViewModel.Event.LoadApplyList.Success -> {
                showApplyItems(event.applies)
            }
        }}
    }

    private fun showApplyItems(applyItems: List<WasteDisposalApplyItem>) {
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