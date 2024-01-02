package ru.internetcloud.workorderapplication.workorders.presentation.navigation

sealed interface WorkOrdersDirections {

    object ToDataSynchronization : WorkOrdersDirections

    data class ToWorkOrder(
        val args: ToWorkOrderArgs
    ) : WorkOrdersDirections
}
