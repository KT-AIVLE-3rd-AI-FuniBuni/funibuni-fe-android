package com.aivle.presentation.searchWasteSpec

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aivle.presentation.databinding.ItemWasteSpecBinding
import com.aivle.presentation.util.search.HangulJamoTextMatcher
import com.loggi.core_util.extensions.log

class WasteSpecListAdapter :
    ListAdapter<FilterableWasteSpec, WasteSpecItemViewHolder>(FilterableWasteSpec.DiffCallback),
    Filterable {

    private val originList = mutableListOf<FilterableWasteSpec>()

    override fun getFilter(): Filter = SearchFilter()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WasteSpecItemViewHolder {
        return createWasteSpecItemViewHolder(parent)
    }

    override fun onBindViewHolder(holder: WasteSpecItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    fun setData(list: List<FilterableWasteSpec>) {
        originList.clear()
        originList.addAll(list)
        submitList(list)
    }

    fun filter(keyword: String) {
        filter.filter(keyword)
    }

    inner class SearchFilter : ListAdapterFilter<FilterableWasteSpec>() {
        override fun performFiltering2(constraint: CharSequence?): List<FilterableWasteSpec> {
            log("performFiltering2(): constraint=$constraint")
            return if (constraint.isNullOrBlank()) {
                originList.onEach { it.resetPoint() }
            } else {
                HangulJamoTextMatcher
                    .matchBy(originList, constraint.toString())
                    .map { it.clone() }
            }
        }

        override fun publishResults2(constraint: CharSequence?, results: List<FilterableWasteSpec>) {
            log("publishResults2(): constraint=$constraint, results.size=${results.size}")
            submitList(results)
        }
    }
}

class WasteSpecItemViewHolder(private val binding: ItemWasteSpecBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: FilterableWasteSpec) {
        binding.item = item
    }
}

private fun createWasteSpecItemViewHolder(parent: ViewGroup) = WasteSpecItemViewHolder(
    ItemWasteSpecBinding.inflate(LayoutInflater.from(parent.context), parent, false)
)