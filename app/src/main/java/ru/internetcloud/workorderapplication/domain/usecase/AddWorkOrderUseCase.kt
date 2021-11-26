package ru.internetcloud.workorderapplication.domain.usecase

import ru.internetcloud.workorderapplication.domain.document.WorkOrder
import ru.internetcloud.workorderapplication.domain.repository.WorkOrderRepositoryInterface

class AddWorkOrderUseCase(private val workOrderRepository: WorkOrderRepositoryInterface) {
    fun addWorkOrder(workOrder: WorkOrder) {
        workOrderRepository.addWorkOrder(workOrder)
    }
}
