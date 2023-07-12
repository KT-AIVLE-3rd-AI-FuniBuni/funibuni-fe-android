package com.aivle.presentation.util.common

data class ListUiState<T>(
    val isLoading: Boolean = false,
    val toastMessage: String? = null,
    val data: List<T> = emptyList(),
)