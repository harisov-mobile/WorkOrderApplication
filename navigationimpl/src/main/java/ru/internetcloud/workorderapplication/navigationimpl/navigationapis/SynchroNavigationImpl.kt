package ru.internetcloud.workorderapplication.navigationimpl.navigationapis

import androidx.navigation.NavController
import javax.inject.Inject
import ru.internetcloud.workorderapplication.navigationapi.NavigationApi
import ru.internetcloud.workorderapplication.synchro.presentation.DataSynchronizationFragmentDirections
import ru.internetcloud.workorderapplication.synchro.presentation.navigation.SynchroDirections

class SynchroNavigationImpl @Inject constructor(
    private val navController: NavController
): NavigationApi<SynchroDirections> {

    override fun navigate(direction: SynchroDirections) {
        when (direction) {
            SynchroDirections.ToWorkOrders -> {
                // запустить фрагмент, где будет список заказ-нарядов
                navController.navigate(DataSynchronizationFragmentDirections.actionDataSynchronizationFragmentToWorkOrderListFragment())
            }
        }
    }
}
