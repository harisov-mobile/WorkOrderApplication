package ru.internetcloud.workorderapplication.domain.usecase

import ru.internetcloud.workorderapplication.domain.document.WorkOrder
import ru.internetcloud.workorderapplication.domain.repository.WorkOrderRepositoryInterface

class UpdateWorkOrderUseCase(private val workOrderRepository: WorkOrderRepositoryInterface) {
    fun updateWorkOrder(workOrder: WorkOrder) {
        return workOrderRepository.updateWorkOrder(workOrder)
    }
}
