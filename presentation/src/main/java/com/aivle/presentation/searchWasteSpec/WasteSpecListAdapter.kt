package com.aivle.presentation.searchWasteSpec

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aivle.domain.model.waste.WasteSpec
import com.aivle.presentation.databinding.ItemWasteSpecBinding
import com.aivle.presentation.util.diffcallback.WasteSpecDiffCallback

class WasteSpecListAdapter : ListAdapter<WasteSpec, WasteSpecItemViewHolder>(WasteSpecDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WasteSpecItemViewHolder {
        return createWasteSpecItemViewHolder(parent)
    }

    override fun onBindViewHolder(holder: WasteSpecItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}

class WasteSpecItemViewHolder(private val binding: ItemWasteSpecBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(wasteSpec: WasteSpec) {
        Log.d("aaa", wasteSpec.toString() + " ${wasteSpec.feeString}")
        binding.wasteSpec = wasteSpec
    }
}

private fun createWasteSpecItemViewHolder(parent: ViewGroup) = WasteSpecItemViewHolder(
    ItemWasteSpecBinding.inflate(LayoutInflater.from(parent.context), parent, false)
)