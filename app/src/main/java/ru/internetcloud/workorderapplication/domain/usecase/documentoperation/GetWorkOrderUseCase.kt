package ru.internetcloud.workorderapplication.domain.usecase.documentoperation

import javax.inject.Inject
import ru.internetcloud.workorderapplication.domain.document.WorkOrder
import ru.internetcloud.workorderapplication.domain.repository.WorkOrderRepository

class GetWorkOrderUseCase @Inject constructor(
    private val workOrderRepository: WorkOrderRepository
) {
    suspend fun getWorkOrder(workOrderId: String): WorkOrder? {
        return workOrderRepository.getWorkOrder(workOrderId)
    }
}
