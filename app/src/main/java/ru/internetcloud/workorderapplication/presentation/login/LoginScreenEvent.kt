package ru.internetcloud.workorderapplication.presentation.login

sealed interface LoginScreenEvent {

    data class ShowMessage(val message: String) : LoginScreenEvent
}
