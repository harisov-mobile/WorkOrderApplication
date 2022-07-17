package ru.internetcloud.workorderapplication.data.model

sealed class RefreshingResult {

    object Success : RefreshingResult()

    data class Error(val exception: Exception) : RefreshingResult()
}
