package ru.internetcloud.workorderapplication.navigationimpl.navigationapis

import androidx.navigation.NavController
import javax.inject.Inject
import javax.inject.Provider
import ru.internetcloud.workorderapplication.navigationapi.NavigationApi
import ru.internetcloud.workorderapplication.workorderdetail.presentation.navigation.WorkOrderDetailDirections

class WorkOrderDetailNavigationImpl @Inject constructor(
    private val navController: Provider<NavController>,
): NavigationApi<WorkOrderDetailDirections> {

    override fun navigate(direction: WorkOrderDetailDirections) {
        when (direction) {
            WorkOrderDetailDirections.Up -> {
                navController.get().navigateUp()
            }
        }
    }
}
