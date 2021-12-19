package ru.internetcloud.workorderapplication.domain.usecase.documentoperation

import androidx.lifecycle.LiveData
import ru.internetcloud.workorderapplication.domain.document.WorkOrder
import ru.internetcloud.workorderapplication.domain.repository.WorkOrderRepository

class GetWorkOrderListUseCase(private val workOrderRepository: WorkOrderRepository) {
    suspend fun getWorkOrderList(): List<WorkOrder> {
        return workOrderRepository.getWorkOrderList()
    }
}
