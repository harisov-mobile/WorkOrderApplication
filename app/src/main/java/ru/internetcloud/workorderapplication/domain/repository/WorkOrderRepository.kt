package ru.internetcloud.workorderapplication.domain.repository

import androidx.lifecycle.LiveData
import ru.internetcloud.workorderapplication.domain.common.SearchWorkOrderData
import ru.internetcloud.workorderapplication.domain.document.WorkOrder

interface WorkOrderRepository {

    fun getWorkOrderList(): LiveData<List<WorkOrder>>

    fun getFilteredWorkOrderList(searchWorkOrderData: SearchWorkOrderData): LiveData<List<WorkOrder>>

    suspend fun getWorkOrder(workOrderId: String): WorkOrder?

    suspend fun updateWorkOrder(workOrder: WorkOrder)
}
