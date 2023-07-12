package com.aivle.presentation.util.common

data class UiState<T>(
    val isLoading: Boolean = false,
    val message: String? = null,
    val data: T? = null,
)