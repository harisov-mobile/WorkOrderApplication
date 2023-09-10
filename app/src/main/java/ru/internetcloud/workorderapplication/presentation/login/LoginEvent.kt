package ru.internetcloud.workorderapplication.presentation.login

sealed interface LoginEvent {

    data class OnServerChange(val server: String) : LoginEvent

    data class OnLoginChange(val login: String) : LoginEvent

    data class OnPasswordChange(val password: String) : LoginEvent

    object OnSignIn : LoginEvent
}
