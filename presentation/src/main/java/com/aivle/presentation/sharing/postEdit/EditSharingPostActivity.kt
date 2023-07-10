package com.aivle.presentation.sharing.postEdit

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import com.aivle.domain.model.sharingPost.SharingPostDetail
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseActivity
import com.aivle.presentation.databinding.ActivityEditSharingPostBinding
import com.aivle.presentation.util.ext.repeatOnStarted
import com.aivle.presentation.util.model.FuniBuniDate
import com.aivle.presentation_design.interactive.ui.BottomUpDialog
import com.aivle.presentation.sharing.postEdit.EditSharingPostViewModel.Event
import com.aivle.presentation.util.common.CategoryImageBinder
import com.aivle.presentation.util.ext.showToast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.wait

@AndroidEntryPoint
class EditSharingPostActivity : BaseActivity<ActivityEditSharingPostBinding>(R.layout.activity_edit_sharing_post) {

    private val viewModel: EditSharingPostViewModel by viewModels()

    private val postId: Int?
        get() = intent.getIntExtra(POST_ID, -1).takeIf { it != -1 }

    private lateinit var sharingExpiredDate: FuniBuniDate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        handleViewModelEvent()

        postId?.let {
            viewModel.loadPost(it)
        }
    }

    override fun onBackPressed() {
        showExitConfirmDialog()
    }

    private fun initView() {
        binding.header.title.text = "게시글 수정"
        binding.header.btnBack.setOnClickListener {
            showExitConfirmDialog()
        }

        val afterWeek = FuniBuniDate.today() + 7
        updateSharingPeriod(afterWeek)

        val textWatcher = MyTextWatcher()
        binding.edtPostTitle.addTextChangedListener(textWatcher)
        binding.edtPostContent.addTextChangedListener(textWatcher)

        binding.btnSharingPeriod.setOnClickListener {
            showDatePickerDialog()
        }
        binding.btnComplete.setOnClickListener {
            updateSharingPost()
        }
    }

    private fun handleViewModelEvent() = repeatOnStarted {
        viewModel.eventFlow.collect { event -> when (event) {
            is Event.None -> {
            }
            is Event.LoadPost.Success -> {
                showPostDetail(event.post)
            }
            is Event.UpdatePost.Success -> {
                setResult(RESULT_OK)
                finish()
            }
            is Event.Failure -> {
                showToast(event.message)
            }
        }}
    }

    private fun showDatePickerDialog() {
        val date = sharingExpiredDate
        val dialog = DatePickerDialog(this, { view, year, month, dayOfMonth ->
            val pickDate = FuniBuniDate(year, month, dayOfMonth)
            updateSharingPeriod(pickDate)
        }, date.year, date.month, date.day)
        dialog.show()
    }

    private fun updateSharingPeriod(date: FuniBuniDate) {
        val diffDay = date - FuniBuniDate.today()
        if (diffDay <= 0) {
            BottomUpDialog.Builder(this)
                .title("나눔 기간은 오늘 이후여야 합니다")
                .confirmedButton()
                .show()
        } else {
            sharingExpiredDate = date
            binding.sharingPeriod.text = "${date.toDateString()}까지 (${diffDay}일)"
        }
    }

    private fun showPostDetail(post: SharingPostDetail) {
        Glide.with(this)
            .load(post.image_url)
            .centerCrop()
            .error(R.drawable.placeholder_1440)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.wasteImage)

        val largeCategoryIcon = CategoryImageBinder.circle(post.product_mid_category)
        binding.wasteCategoryIcon.setImageResource(largeCategoryIcon)
        binding.wasteCategoryName.text = "${post.product_mid_category} (${post.product_low_category})"

        binding.edtPostTitle.setText(post.title)
        binding.edtPostContent.setText(post.content)

        updateSharingPeriod(FuniBuniDate.parse(post.expired_date))
    }

    private fun updateSharingPost() {
        val title = binding.edtPostTitle.text.toString()
        val content = binding.edtPostContent.text.toString()
        val expiredDate = sharingExpiredDate.toServerFormat()

        if (title.isNotBlank() && content.isNotBlank()) {
            viewModel.updatePost(title, content, expiredDate)
        }
    }

    private fun showExitConfirmDialog() {
        if (binding.edtPostTitle.text.toString() == viewModel.post?.title &&
                binding.edtPostContent.text.toString() == viewModel.post?.content) {
            finish()
            return
        }
        BottomUpDialog.Builder(this)
            .title("작성을 중단하시겠습니까?")
            .subtitle("작성중인 내용은 저장되지 않습니다")
            .positiveButton {
                finish()
            }
            .show()
    }

    companion object {

        const val POST_ID = "post_id"

        fun getIntent(context: Context, postId: Int) = Intent(context, EditSharingPostActivity::class.java).apply {
            putExtra(POST_ID, postId)
        }
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