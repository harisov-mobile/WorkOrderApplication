package ru.internetcloud.workorderapplication.workorderdetail.presentation.navigation

sealed interface WorkOrderDetailDirections {

    object Up : WorkOrderDetailDirections // это действие "назад"
}
