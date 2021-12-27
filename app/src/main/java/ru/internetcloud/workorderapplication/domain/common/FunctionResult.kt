package ru.internetcloud.workorderapplication.domain.common

data class FunctionResult(
    var isSuccess: Boolean = false,
    var errorMessage: String = ""
)
