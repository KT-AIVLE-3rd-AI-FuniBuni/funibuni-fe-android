package com.aivle.presentation.searchWasteSpec

import androidx.recyclerview.widget.DefaultItemAnimator

class MyItemAnimator : DefaultItemAnimator() {

    override fun getMoveDuration(): Long = super.getMoveDuration()

    override fun getAddDuration(): Long = super.getAddDuration()

    override fun getRemoveDuration(): Long = super.getRemoveDuration()

    override fun getChangeDuration(): Long = super.getChangeDuration()
}