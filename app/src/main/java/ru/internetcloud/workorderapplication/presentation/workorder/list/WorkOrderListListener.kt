package ru.internetcloud.workorderapplication.presentation.workorder.list

import ru.internetcloud.workorderapplication.domain.document.WorkOrder

interface WorkOrderListListener {

    fun onItemClick(workOrder: WorkOrder)
}
