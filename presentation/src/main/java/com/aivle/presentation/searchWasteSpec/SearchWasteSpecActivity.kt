package com.aivle.presentation.searchWasteSpec

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseActivity
import com.aivle.presentation.databinding.ActivitySearchWasteSpecBinding
import com.aivle.presentation.util.ext.repeatOnStarted
import com.aivle.presentation.searchWasteSpec.SearchWasteSpecViewModel.Event
import com.aivle.presentation.util.ext.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchWasteSpecActivity : BaseActivity<ActivitySearchWasteSpecBinding>(R.layout.activity_search_waste_spec) {

    private val viewModel: SearchWasteSpecViewModel by viewModels()
    private lateinit var listAdapter: WasteSpecListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        handleViewModelEvent()

        viewModel.loadWasteSpecTable()
    }

    private fun initView() {
        binding.btnBack.setOnClickListener { finish() }
        binding.btnSearch.setOnClickListener {
            search()
        }
        binding.edtSearch.addTextChangedListener {
            search()
        }
        binding.edtSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search()
            }
            false
        }
        listAdapter = WasteSpecListAdapter()
        binding.listView.adapter = listAdapter
        binding.listView.itemAnimator = MyItemAnimator()
    }

    private fun search() {
        val keyword = binding.edtSearch.text.toString()
        listAdapter.filter(keyword)
        // viewModel.search(keyword)
    }

    private fun handleViewModelEvent() = repeatOnStarted {
        viewModel.eventFlow.collect { event -> when (event) {
            is Event.None -> {
            }
            is Event.Failure -> {
                showToast(event.message)
            }
            is Event.LoadedWasteSpecTable -> {
                listAdapter.setData(event.table.map(::FilterableWasteSpec))
            }
            is Event.SearchWasteSpecResult -> {
                // listAdapter.submitList(event.results)
            }
        }}
    }

    companion object {
        fun getIntent(context: Context) = Intent(context, SearchWasteSpecActivity::class.java)
    }
}