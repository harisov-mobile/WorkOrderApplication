package ru.internetcloud.workorderapplication.workorders.domain.usecase

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import ru.internetcloud.workorderapplication.common.domain.model.document.WorkOrder
import ru.internetcloud.workorderapplication.common.domain.repository.WorkOrderRepository

class GetWorkOrderListUseCase @Inject constructor(
    private val workOrderRepository: WorkOrderRepository
) {
    fun getWorkOrderList(): Flow<List<WorkOrder>> {
        return workOrderRepository.getWorkOrderList()
    }
}
