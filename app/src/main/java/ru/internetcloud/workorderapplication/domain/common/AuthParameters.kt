package ru.internetcloud.workorderapplication.domain.common

data class AuthParameters(
    var server: String = "",
    var login: String = "",
    var password: String = ""
)
