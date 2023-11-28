package ru.internetcloud.workorderapplication.domain.usecase.documentoperation

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import ru.internetcloud.workorderapplication.domain.model.document.WorkOrder
import ru.internetcloud.workorderapplication.domain.repository.WorkOrderRepository

class GetWorkOrderListUseCase @Inject constructor(
    private val workOrderRepository: WorkOrderRepository
) {
    fun getWorkOrderList(): Flow<List<WorkOrder>> {
        return workOrderRepository.getWorkOrderList()
    }
}
