package ru.internetcloud.workorderapplication.common.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.internetcloud.workorderapplication.common.domain.common.SearchWorkOrderData
import ru.internetcloud.workorderapplication.common.domain.model.document.WorkOrder

interface WorkOrderRepository {

    fun getWorkOrderList(): Flow<List<WorkOrder>>

    fun getFilteredWorkOrderList(searchWorkOrderData: SearchWorkOrderData): Flow<List<WorkOrder>>

    suspend fun getWorkOrder(workOrderId: String): WorkOrder?

    suspend fun getDuplicateWorkOrderByNumber(number: String, workOrderId: String): WorkOrder?

    suspend fun updateWorkOrder(workOrder: WorkOrder)
}
