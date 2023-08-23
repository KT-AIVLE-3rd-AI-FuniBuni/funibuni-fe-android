package com.aivle.presentation.intro.sign

import android.Manifest
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.EditorInfo
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.Guideline
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.aivle.domain.model.kakao.KakaoAddressDocument
import com.aivle.presentation.R
import com.aivle.presentation.databinding.FragmentSignUpInputAddressBinding
import com.aivle.presentation.intro.sign.SignUpInputAddressViewModel.Event
import com.aivle.presentation.util.ext.dpToPixels
import com.aivle.presentation.util.ext.repeatOnStarted
import com.aivle.presentation.util.ext.showToast
import com.aivle.presentation_design.interactive.customView.BottomUpDialog
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.loggi.core_util.extensions.log
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpInputAddressFragment : BaseSignFragment<FragmentSignUpInputAddressBinding>(R.layout.fragment_sign_up_input_address) {

    override val bottomButtonGuideLine: Guideline? = null

    private val viewModel: SignUpInputAddressViewModel by viewModels()

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var addressListAdapter: KakaoAddressListAdapter

    private var uiState: ContentUiState = ContentUiState.GuideMessageShown

    private val isInitialUiState: Boolean
        get() = uiState == ContentUiState.GuideMessageShown

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (isInitialUiState) {
                (requireActivity() as SignActivity).moveBackPage()
            } else {
                updateUiState(ContentUiState.GuideMessageShown)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        log("onViewCreated()")

        fusedLocationClient = LocationServices
            .getFusedLocationProviderClient(requireActivity())

        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, backPressedCallback)

        initView()
        handleUiEvent()
    }

    override fun onResume() {
        super.onResume()
        backPressedCallback.isEnabled = true
    }

    override fun onPause() {
        super.onPause()
        backPressedCallback.isEnabled = false
    }

    private fun initView() {
        // binding.header.layoutTransition.setDuration(LayoutTransition.DISAPPEARING, 100L)
        binding.btnCurrentLocation.setOnClickListener {
            checkLocationPermissions()
        }
        binding.listView.adapter = KakaoAddressListAdapter().also {
            addressListAdapter = it
        }
        binding.edtAddress.setOnFocusChangeListener { v, hasFocus ->
            if (isInitialUiState && hasFocus) {
                updateUiState(ContentUiState.GuideMessageHidden)
            }
        }
        binding.edtAddress.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val address = binding.edtAddress.text.toString()
                if (address.isBlank()) {
                    showToast("검색어를 입력해주세요")
                    true
                } else {
                    viewModel.searchAddress(address)
                    false
                }
            } else {
                false
            }
        }
    }

    private fun handleUiEvent() = repeatOnStarted {
        viewModel.eventFlow.collect { event ->
            if (event is Event.Loading) {
                binding.progressIndicator.show()
            } else {
                binding.progressIndicator.hide()
            }

            when (event) {
                is Event.Init -> {
                }
                is Event.Loading -> {
                }
                is Event.Success.SearchAddress -> {
                    updateUiState(ContentUiState.SearchResult)

                    val documents = event.documents.onEach {
                        it.onClick = ::moveNextDetailPage
                    }
                    addressListAdapter.submitList(documents)
                }
                is Event.Success.CoordinateToAddress -> {
                    updateUiState(ContentUiState.SearchResult)

                    binding.edtAddress.setText(event.addressName)
                }
                is Event.Success.Empty -> {
                    updateUiState(ContentUiState.NoSearchResult)

                    addressListAdapter.submitList(emptyList())
                }
                is Event.Failure -> {
                    showToast(event.message)
                }
            }
        }
    }

    private fun updateUiState(contentUiState: ContentUiState) {
        if (uiState == contentUiState) {
            return
        }
        if (isShowingKeyboard) {
            hideKeyboard()
        }

        when (contentUiState) {
            is ContentUiState.GuideMessageShown -> {
                updateVisibleGuideMessage(true)
                binding.background.isVisible = true
                binding.noContent.isVisible = false
            }
            is ContentUiState.GuideMessageHidden -> {
                updateVisibleGuideMessage(false)
                binding.noContent.isVisible = false
            }
            is ContentUiState.SearchResult -> {
                updateVisibleGuideMessage(false)
                binding.background.isVisible = false
                binding.noContent.isVisible = false
            }
            is ContentUiState.NoSearchResult -> {
                updateVisibleGuideMessage(false)
                binding.background.isVisible = false
                binding.noContent.isVisible = true
            }
        }

        uiState = contentUiState
    }

    private fun updateVisibleGuideMessage(isShow: Boolean) {
        if (isShow == isInitialUiState) {
            return
        }
        if (isShow) {
            binding.edtAddress.text?.clear()
            binding.edtAddress.clearFocus()
            addressListAdapter.submitList(emptyList())
        }

        val from = if (isShow) 0 else dp(80)
        val dest = if (isShow) dp(80) else 0

        ValueAnimator.ofInt(from, dest).apply {
            interpolator = AccelerateDecelerateInterpolator()
            duration = 250L
            addUpdateListener {
                binding.guidelineAddress.setGuidelineBegin(it.animatedValue as Int)
            }
        }.start()

        binding.guideMessageContainer.animate()
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setDuration(150L)
            .alpha(if (isShow) 1f else 0f)
            .start()
    }

    private fun moveNextDetailPage(address: KakaoAddressDocument) {
        log("moveNextDetailPage(): $address")
        if (address.road_address == null) {
            return
        }
        if (address.road_address!!.region_2depth_name == "송파구") {
            signViewModel.sendAddress(address)
            moveNextPage()
        } else {
            showDialog()
        }
    }

    private fun showDialog() {
        BottomUpDialog.Builder(requireActivity())
            .title("아쉽게도 퍼니버니 서비스는 현재 송파구 지역만 지원하고 있습니다😭")
            .subtitle("송파구 주소가 없으시면 송파구청 주소로 임의 등록을 도와드릴까요?")
            .positiveButton {
                val address = "송파구 올림픽로 326"
                binding.edtAddress.setText(address)
                viewModel.searchAddress(address)
            }
            .show()
    }

    private fun checkLocationPermissions() {
        TedPermission.create()
            .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    getLastLocation()
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    showToast("onPermissionGranted(): $deniedPermissions")
                }
            })
            .check()
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener {
            viewModel.coordinateToAddress(it.longitude, it.latitude)
        }
    }

    private fun dp(pixels: Int): Int {
        return pixels.dpToPixels(requireContext())
    }

    private sealed class ContentUiState {
        object GuideMessageShown : ContentUiState()
        object GuideMessageHidden : ContentUiState()
        object SearchResult : ContentUiState()
        object NoSearchResult : ContentUiState()
    }
}