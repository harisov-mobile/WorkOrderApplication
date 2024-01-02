package ru.internetcloud.workorderapplication.login.presentation.navigation

sealed interface LoginDirections {

    object ToDataSynchronization : LoginDirections

    object ToWorkOrders : LoginDirections
}
