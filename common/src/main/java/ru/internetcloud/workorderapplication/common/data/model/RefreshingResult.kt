package ru.internetcloud.workorderapplication.common.data.model

sealed class RefreshingResult {

    object Success : RefreshingResult()

    data class Error(val exception: Exception) : RefreshingResult()
}
