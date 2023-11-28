package ru.internetcloud.workorderapplication.presentation.util

sealed interface UiState<out T> {

    data class Success<out T>(
        val data: T,
        val isNew: Boolean = false
    ) : UiState<T>

    data class Error(val exception: Throwable) : UiState<Nothing>

    object Loading : UiState<Nothing>

    object EmptyData : UiState<Nothing>
}
