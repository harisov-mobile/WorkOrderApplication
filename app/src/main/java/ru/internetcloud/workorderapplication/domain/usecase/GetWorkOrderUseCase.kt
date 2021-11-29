package ru.internetcloud.workorderapplication.domain.usecase

import java.util.UUID
import ru.internetcloud.workorderapplication.domain.document.WorkOrder
import ru.internetcloud.workorderapplication.domain.repository.WorkOrderRepositoryInterface

class GetWorkOrderUseCase(private val workOrderRepository: WorkOrderRepositoryInterface) {
    fun getWorkOrder(id: UUID): WorkOrder? {
        return workOrderRepository.getWorkOrder(id)
    }
}
