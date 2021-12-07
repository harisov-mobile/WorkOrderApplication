package ru.internetcloud.workorderapplication.domain.repository

import androidx.lifecycle.LiveData
import ru.internetcloud.workorderapplication.domain.document.WorkOrder

interface WorkOrderRepository {
    fun getWorkOrderList(): LiveData<List<WorkOrder>>

    suspend fun addWorkOrder(workOrder: WorkOrder)

    suspend fun updateWorkOrder(workOrder: WorkOrder)

    suspend fun getWorkOrder(id: Int): WorkOrder?
}
