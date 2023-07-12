package com.aivle.presentation.util.common

data class ListUiState<T>(
    val isLoading: Boolean = false,
    val message: String? = null,
    val data: List<T> = emptyList(),
)