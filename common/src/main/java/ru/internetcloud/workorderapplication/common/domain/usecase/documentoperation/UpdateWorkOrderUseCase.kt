package ru.internetcloud.workorderapplication.common.domain.usecase.documentoperation

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.domain.model.document.WorkOrder
import ru.internetcloud.workorderapplication.common.domain.repository.WorkOrderRepository

class UpdateWorkOrderUseCase @Inject constructor(
    private val workOrderRepository: WorkOrderRepository
) {
    suspend fun updateWorkOrder(workOrder: WorkOrder) {
        return workOrderRepository.updateWorkOrder(workOrder)
    }
}
