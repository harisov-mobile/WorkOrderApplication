package ru.internetcloud.workorderapplication.presentation.workorder.list

import ru.internetcloud.workorderapplication.domain.model.document.WorkOrder

interface WorkOrderListListener {
    fun onItemClick(workOrder: WorkOrder)
}
