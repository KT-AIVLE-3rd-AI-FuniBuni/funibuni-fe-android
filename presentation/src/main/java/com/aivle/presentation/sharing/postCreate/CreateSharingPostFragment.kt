package com.aivle.presentation.sharing.postCreate

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aivle.domain.model.sharingPost.SharingPostCreate
import com.aivle.presentation.R
import com.aivle.presentation.databinding.FragmentCreateSharingPostBinding
import com.aivle.presentation.disposal.base.BaseDisposalFragment
import com.aivle.presentation.util.ext.repeatOnStarted
import com.aivle.presentation.util.model.FuniBuniDate
import com.aivle.presentation_design.interactive.ui.BottomUpDialog
import com.aivle.presentation.sharing.postCreate.CreateSharingPostViewModel.Event
import com.aivle.presentation.sharing.postDetail.SharingPostDetailActivity
import com.aivle.presentation.util.ext.showToast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "CreateSharingPostFragment"

@AndroidEntryPoint
class CreateSharingPostFragment
    : BaseDisposalFragment<FragmentCreateSharingPostBinding>(R.layout.fragment_create_sharing_post) {

    private val viewModel: CreateSharingPostViewModel by viewModels()

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            Log.d(TAG, "handleOnBackPressed()")
            showBackConfirmDialog()
        }
    }

    private lateinit var sharingExpiredDate: FuniBuniDate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, backPressedCallback)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        backPressedCallback.isEnabled = true
    }

    override fun onPause() {
        super.onPause()
        backPressedCallback.isEnabled = true
    }

    override fun onDetach() {
        super.onDetach()
        backPressedCallback.remove()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        handleViewModelEvent()
    }

    private fun initView() {
        val classificationResult = activityViewModel.classificationResult ?: return
        val wasteSpec = activityViewModel.selectedWasteSpec ?: return

        Glide.with(requireContext())
            .load(classificationResult.imageUrl)
            .centerCrop()
            .error(R.drawable.placeholder_1440)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.wasteImage)

        val afterWeek = FuniBuniDate.today() + 7
        updateSharingPeriod(afterWeek)

        val textWatcher = MyTextWatcher()
        binding.edtPostTitle.addTextChangedListener(textWatcher)
        binding.edtPostContent.addTextChangedListener(textWatcher)

        binding.btnSharingPeriod.setOnClickListener {
            showDatePickerDialog()
        }
        binding.btnComplete.isEnabled = false
        binding.btnComplete.setOnClickListener {
            createSharingPost()
        }
    }

    private fun handleViewModelEvent() = repeatOnStarted {
        viewModel.eventFlow.collect { event -> when (event) {
            is Event.None -> {
            }
            is Event.CreatePost.Success -> {
                movePostDetail(event.newPostId)
            }
            is Event.Failure -> {
                showToast(event.message)
            }
        } }
    }

    private fun showDatePickerDialog() {
        val date = sharingExpiredDate
        val dialog = DatePickerDialog(requireContext(), { view, year, month, dayOfMonth ->
            val pickDate = FuniBuniDate(year, month, dayOfMonth)
            updateSharingPeriod(pickDate)
        }, date.year, date.month, date.day)
        dialog.show()
    }

    private fun showBackConfirmDialog() {
        if (binding.edtPostTitle.text.isBlank() && binding.edtPostContent.text.isBlank()) {
            goBackFragment()
            return
        }
        BottomUpDialog.Builder(requireActivity())
            .title("나눔 게시물 작성을 그만하시겠습니까?")
            .subtitle("작성된 내용은 모두 사라집니다")
            .positiveButton("계속하기")
            .negativeButton("그만하기") {
                goBackFragment()
            }
            .show()
    }

    private fun updateSharingPeriod(date: FuniBuniDate) {
        val diffDay = date - FuniBuniDate.today()
        if (diffDay <= 0) {
            BottomUpDialog.Builder(requireActivity())
                .title("나눔 기간은 오늘 이후여야 합니다")
                .confirmedButton()
                .show()
        } else {
            sharingExpiredDate = date
            binding.sharingPeriod.text = "${date.toDateString()}까지 (${diffDay}일)"
        }
    }

    private fun createSharingPost() {
        val result = activityViewModel.classificationResult ?: return
        val wasteSpec = activityViewModel.selectedWasteSpec ?: return
        val address = activityViewModel.address ?: return

        val newPost = SharingPostCreate(
            binding.edtPostTitle.text.toString(),
            binding.edtPostContent.text.toString(),
            sharingExpiredDate.toServerFormat(),
            result.imageUrl,
            wasteSpec.top_category,
            wasteSpec.large_category,
            wasteSpec.small_category,
            address.city,
            address.district,
            address.dong,
        )
        viewModel.createSharingPost(newPost)
    }

    private fun goBackFragment() {
        findNavController().popBackStack(R.id.applyChoiceFragment, false)
    }

    private fun movePostDetail(postId: Int) {
        requireActivity().finish()
        startActivity(SharingPostDetailActivity.getIntent(requireContext(), postId))
    }

    inner class MyTextWatcher : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun afterTextChanged(s: Editable?) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            binding.btnComplete.isEnabled = (binding.edtPostTitle.text.isNotBlank() && binding.edtPostContent.text.isNotBlank())
        }
    }
}