package ru.internetcloud.workorderapplication.domain.usecase.documentoperation

import ru.internetcloud.workorderapplication.domain.document.WorkOrder
import ru.internetcloud.workorderapplication.domain.repository.WorkOrderRepository

class GetWorkOrderUseCase(private val workOrderRepository: WorkOrderRepository) {
    suspend fun getWorkOrder(id: Int): WorkOrder? {
        return workOrderRepository.getWorkOrder(id)
    }
}
