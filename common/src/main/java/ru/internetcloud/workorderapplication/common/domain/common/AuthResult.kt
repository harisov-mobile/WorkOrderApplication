package ru.internetcloud.workorderapplication.common.domain.common

data class AuthResult(
    var isAuthorized: Boolean,
    var errorMessage: String
)
