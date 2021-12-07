package ru.internetcloud.workorderapplication.domain.usecase

import ru.internetcloud.workorderapplication.domain.document.WorkOrder
import ru.internetcloud.workorderapplication.domain.repository.WorkOrderRepository

class AddWorkOrderUseCase(private val workOrderRepository: WorkOrderRepository) {
    suspend fun addWorkOrder(workOrder: WorkOrder) {
        workOrderRepository.addWorkOrder(workOrder)
    }
}
