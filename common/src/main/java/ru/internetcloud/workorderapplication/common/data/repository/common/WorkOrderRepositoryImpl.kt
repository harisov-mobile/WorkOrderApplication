package ru.internetcloud.workorderapplication.common.data.repository.common

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import ru.internetcloud.workorderapplication.common.data.datasource.local.WorkOrderLocalDataSource
import ru.internetcloud.workorderapplication.common.domain.common.SearchWorkOrderData
import ru.internetcloud.workorderapplication.common.domain.model.document.WorkOrder
import ru.internetcloud.workorderapplication.common.domain.repository.WorkOrderRepository

class WorkOrderRepositoryImpl @Inject constructor(
    private val workOrderLocalDataSource: WorkOrderLocalDataSource
) : WorkOrderRepository {

    override fun getWorkOrderList(): Flow<List<WorkOrder>> {
        return workOrderLocalDataSource.getWorkOrderList()
    }

    override fun getFilteredWorkOrderList(searchWorkOrderData: SearchWorkOrderData): Flow<List<WorkOrder>> {
        return workOrderLocalDataSource.getFilteredWorkOrderList(searchWorkOrderData)
    }

    override suspend fun getWorkOrder(workOrderId: String): WorkOrder? {
        return workOrderLocalDataSource.getWorkOrder(workOrderId)
    }

    override suspend fun getDuplicateWorkOrderByNumber(number: String, workOrderId: String): WorkOrder? {
        return workOrderLocalDataSource.getDuplicateWorkOrderByNumber(number = number, workOrderId = workOrderId)
    }

    override suspend fun updateWorkOrder(workOrder: WorkOrder) {
        workOrderLocalDataSource.updateWorkOrder(workOrder)
    }
}
