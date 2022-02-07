package ru.internetcloud.workorderapplication.domain.common

data class AuthResult(
    var isAuthorized: Boolean,
    var errorMessage: String
)
