package ru.internetcloud.workorderapplication.domain.repository

import androidx.lifecycle.LiveData
import ru.internetcloud.workorderapplication.domain.document.WorkOrder

interface WorkOrderRepository {

    fun getWorkOrderList(): LiveData<List<WorkOrder>>

    suspend fun getWorkOrder(workOrderId: String): WorkOrder?

    suspend fun updateWorkOrder(workOrder: WorkOrder)
}
