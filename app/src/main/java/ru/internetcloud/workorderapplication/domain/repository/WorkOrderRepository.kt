package ru.internetcloud.workorderapplication.domain.repository

import androidx.lifecycle.LiveData
import ru.internetcloud.workorderapplication.domain.document.WorkOrder

interface WorkOrderRepository {

    suspend fun getWorkOrderList(): List<WorkOrder>

    suspend fun getWorkOrder(workOrderId: String): WorkOrder?

    suspend fun addWorkOrder(workOrder: WorkOrder)

    suspend fun updateWorkOrder(workOrder: WorkOrder)
}
