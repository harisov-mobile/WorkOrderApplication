package ru.internetcloud.workorderapplication.domain.usecase.documentoperation

import ru.internetcloud.workorderapplication.domain.document.WorkOrder
import ru.internetcloud.workorderapplication.domain.repository.WorkOrderRepository
import javax.inject.Inject

class GetWorkOrderUseCase @Inject constructor(
    private val workOrderRepository: WorkOrderRepository
) {
    suspend fun getWorkOrder(workOrderId: String): WorkOrder? {
        return workOrderRepository.getWorkOrder(workOrderId)
    }
}
