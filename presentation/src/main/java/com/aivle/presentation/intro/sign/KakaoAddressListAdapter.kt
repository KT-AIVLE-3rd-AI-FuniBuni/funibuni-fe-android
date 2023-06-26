package com.aivle.presentation.intro.sign

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aivle.domain.model.kakao.KakaoAddressDocument
import com.aivle.presentation.databinding.ItemKakaoAddressBinding

private val DiffCallback = object : DiffUtil.ItemCallback<KakaoAddressDocument>() {
    override fun areItemsTheSame(
        oldItem: KakaoAddressDocument,
        newItem: KakaoAddressDocument
    ): Boolean = oldItem === newItem

    override fun areContentsTheSame(
        oldItem: KakaoAddressDocument,
        newItem: KakaoAddressDocument
    ): Boolean = oldItem == newItem
}

class KakaoAddressListAdapter : ListAdapter<KakaoAddressDocument, KakaoAddressViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KakaoAddressViewHolder {
        return createKakaoAddressViewHolder(parent)
    }

    override fun onBindViewHolder(holder: KakaoAddressViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}

class KakaoAddressViewHolder(private val binding: ItemKakaoAddressBinding) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            val address = binding.address ?: return@setOnClickListener
            address.onClick?.invoke(address)
        }
    }

    fun bind(address: KakaoAddressDocument) {
        binding.address = address
        binding.executePendingBindings()
    }
}

private fun createKakaoAddressViewHolder(parent: ViewGroup) = KakaoAddressViewHolder(
    ItemKakaoAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
)