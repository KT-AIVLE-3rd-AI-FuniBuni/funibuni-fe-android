package com.aivle.presentation.util.common

sealed class UiState<out T> {
    object Loading: UiState<Nothing>()
    data class Success<T>(val data: T): UiState<T>()
    data class Failure(val message: String?): UiState<Nothing>()
}

fun <T> UiState<T>.successOrNull(): T? = if (this is UiState.Success<T>) {
    data
} else {
    null
}

fun <T> UiState<T>.errorMessageOrNull(): String? = if (this is UiState.Failure) {
    message
} else {
    null
}