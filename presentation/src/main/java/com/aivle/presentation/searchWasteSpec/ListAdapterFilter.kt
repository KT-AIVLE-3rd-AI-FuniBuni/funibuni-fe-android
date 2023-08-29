package com.aivle.presentation.searchWasteSpec

import android.widget.Filter

abstract class ListAdapterFilter<T> : Filter() {

    protected abstract fun performFiltering2(constraint: CharSequence?): List<T>
    protected abstract fun publishResults2(constraint: CharSequence?, results: List<T>)

    final override fun performFiltering(constraint: CharSequence?): FilterResults =
        FilterResults().apply {
            val newList = performFiltering2(constraint)
            count = newList.size
            values = newList
        }

    final override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
        if (results == null) {
            publishResults2(constraint, emptyList())
        } else {
            publishResults2(constraint, results.values as List<T>)
        }
    }
}