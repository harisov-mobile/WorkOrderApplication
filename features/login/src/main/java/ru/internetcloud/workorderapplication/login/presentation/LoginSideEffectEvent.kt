package ru.internetcloud.workorderapplication.login.presentation

sealed interface LoginSideEffectEvent {

    data class ShowMessage(val message: String) : LoginSideEffectEvent
}
