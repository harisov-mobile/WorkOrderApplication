package ru.internetcloud.workorderapplication.domain.usecase.documentoperation

import javax.inject.Inject
import ru.internetcloud.workorderapplication.domain.model.document.WorkOrder
import ru.internetcloud.workorderapplication.domain.repository.WorkOrderRepository

class GetDuplicateWorkOrderByNumberUseCase @Inject constructor(
    private val workOrderRepository: WorkOrderRepository
) {
    suspend fun getDuplicateWorkOrderByNumber(number: String, workOrderId: String): WorkOrder? {
        return workOrderRepository.getDuplicateWorkOrderByNumber(number = number, workOrderId = workOrderId)
    }
}
