package ru.internetcloud.workorderapplication.navigationimpl.navigationapis

import androidx.navigation.NavController
import javax.inject.Inject
import ru.internetcloud.workorderapplication.navigationapi.NavigationApi
import ru.internetcloud.workorderapplication.navigationimpl.mapper.WorkOrderDetailArgsMapper
import ru.internetcloud.workorderapplication.workorders.presentation.WorkOrderListFragmentDirections
import ru.internetcloud.workorderapplication.workorders.presentation.navigation.WorkOrdersDirections

class WorkOrdersNavigationImpl @Inject constructor(
    private val navController: NavController,
    private val workOrderDetailArgsMapper: WorkOrderDetailArgsMapper
) : NavigationApi<WorkOrdersDirections> {

    override fun navigate(direction: WorkOrdersDirections) {
        when (direction) {
            WorkOrdersDirections.ToDataSynchronization -> {
                navController.navigate(
                    WorkOrderListFragmentDirections.actionWorkOrderListFragmentToDataSynchronizationFragment()
                )
            }

            is WorkOrdersDirections.ToWorkOrder -> {
                navController.navigate(
                    WorkOrderListFragmentDirections.actionWorkOrderListFragmentToWorkOrderFragment(
                        args = workOrderDetailArgsMapper.map(direction.args)
                    )
                )
            }
        }
    }
}
