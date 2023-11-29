package ru.internetcloud.workorderapplication.data.repository.common

import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import ru.internetcloud.workorderapplication.data.datasource.local.WorkOrderLocalDataSource
import ru.internetcloud.workorderapplication.domain.common.SearchWorkOrderData
import ru.internetcloud.workorderapplication.domain.model.document.WorkOrder
import ru.internetcloud.workorderapplication.domain.repository.WorkOrderRepository

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
        delay(7000)
        return workOrderLocalDataSource.getWorkOrder(workOrderId)
    }

    override suspend fun updateWorkOrder(workOrder: WorkOrder) {
        delay(7000)
        workOrderLocalDataSource.updateWorkOrder(workOrder)
    }
}
