package ru.internetcloud.workorderapplication.presentation.login

sealed interface LoginSideEffectEvent {

    data class ShowMessage(val message: String) : LoginSideEffectEvent
}
