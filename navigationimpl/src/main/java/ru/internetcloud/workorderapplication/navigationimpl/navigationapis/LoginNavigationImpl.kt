package ru.internetcloud.workorderapplication.navigationimpl.navigationapis

import androidx.navigation.NavController
import javax.inject.Inject
import ru.internetcloud.workorderapplication.login.presentation.LoginFragmentDirections
import ru.internetcloud.workorderapplication.login.presentation.navigation.LoginDirections
import ru.internetcloud.workorderapplication.navigationapi.NavigationApi

class LoginNavigationImpl @Inject constructor(
    private val navController: NavController
): NavigationApi<LoginDirections> {

    override fun navigate(direction: LoginDirections) {
        when (direction) {
            LoginDirections.ToDataSynchronization -> {
                // запустить фрагмент, где будет синхронизация данных из 1С
                navController.navigate(LoginFragmentDirections.actionLoginFragmentToDataSynchronizationFragment())
            }
            LoginDirections.ToWorkOrders -> {
                // демо-режим - переход в список Заказ-нарядов:
                // запустить фрагмент, где будет показан список демо-заказ-нарядов
                navController.navigate(LoginFragmentDirections.actionLoginFragmentToWorkOrderListFragment())
            }
        }
    }
}
