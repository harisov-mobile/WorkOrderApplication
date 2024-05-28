package ru.internetcloud.workorderapplication.workorders.presentation

import ru.internetcloud.workorderapplication.common.domain.model.document.WorkOrder

interface WorkOrderListListener {
    fun onItemClick(workOrder: WorkOrder)
}
