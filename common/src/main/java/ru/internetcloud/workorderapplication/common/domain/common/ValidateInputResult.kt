package ru.internetcloud.workorderapplication.common.domain.common

data class ValidateInputResult(
    var isValid: Boolean,
    var errorMessage: String
)
