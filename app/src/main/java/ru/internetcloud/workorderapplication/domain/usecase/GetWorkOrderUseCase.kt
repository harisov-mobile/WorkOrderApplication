package ru.internetcloud.workorderapplication.domain.usecase

import ru.internetcloud.workorderapplication.domain.document.WorkOrder
import ru.internetcloud.workorderapplication.domain.repository.WorkOrderRepository

class GetWorkOrderUseCase(private val workOrderRepository: WorkOrderRepository) {
    fun getWorkOrder(id: Int): WorkOrder? {
        return workOrderRepository.getWorkOrder(id)
    }
}
