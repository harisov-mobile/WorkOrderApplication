package ru.internetcloud.workorderapplication.domain.usecase.documentoperation

import ru.internetcloud.workorderapplication.domain.document.WorkOrder
import ru.internetcloud.workorderapplication.domain.repository.WorkOrderRepository

class UpdateWorkOrderUseCase(private val workOrderRepository: WorkOrderRepository) {
    suspend fun updateWorkOrder(workOrder: WorkOrder) {
        return workOrderRepository.updateWorkOrder(workOrder)
    }
}
