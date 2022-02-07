package ru.internetcloud.workorderapplication.domain.common

data class ValidateInputResult(
    var isValid: Boolean,
    var errorMessage: String
)
