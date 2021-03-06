package ru.internetcloud.workorderapplication.data.repository.common

import androidx.lifecycle.LiveData
import javax.inject.Inject
import ru.internetcloud.workorderapplication.data.datasource.local.WorkOrderLocalDataSource
import ru.internetcloud.workorderapplication.domain.common.SearchWorkOrderData
import ru.internetcloud.workorderapplication.domain.document.WorkOrder
import ru.internetcloud.workorderapplication.domain.repository.WorkOrderRepository

class WorkOrderRepositoryImpl @Inject constructor(
    private val workOrderLocalDataSource: WorkOrderLocalDataSource
) : WorkOrderRepository {

    override fun getWorkOrderList(): LiveData<List<WorkOrder>> {
        return workOrderLocalDataSource.getWorkOrderList()
    }

    override fun getFilteredWorkOrderList(searchWorkOrderData: SearchWorkOrderData): LiveData<List<WorkOrder>> {
        return workOrderLocalDataSource.getFilteredWorkOrderList(searchWorkOrderData)
    }

    override suspend fun getWorkOrder(workOrderId: String): WorkOrder? {
        return workOrderLocalDataSource.getWorkOrder(workOrderId)
    }

    override suspend fun updateWorkOrder(workOrder: WorkOrder) {
        workOrderLocalDataSource.updateWorkOrder(workOrder)
    }
}
