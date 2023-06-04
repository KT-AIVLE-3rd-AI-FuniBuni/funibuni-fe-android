package com.aivle.presentation.address

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseFragment
import com.aivle.presentation.databinding.FragmentAddressBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressFragment : BaseFragment<FragmentAddressBinding>(R.layout.fragment_address) {

    private val viewModel: AddressViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAddress()

        binding.btnInput.setOnClickListener {
            val address = binding.edtAddress.text.toString()
            if (address.isNotBlank()) {
                viewModel.setAddress(address)
            }
        }
    }
}