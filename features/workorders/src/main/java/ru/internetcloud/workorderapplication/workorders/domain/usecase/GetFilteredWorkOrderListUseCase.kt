package ru.internetcloud.workorderapplication.workorders.domain.usecase

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import ru.internetcloud.workorderapplication.common.domain.common.SearchWorkOrderData
import ru.internetcloud.workorderapplication.common.domain.model.document.WorkOrder
import ru.internetcloud.workorderapplication.common.domain.repository.WorkOrderRepository

class GetFilteredWorkOrderListUseCase @Inject constructor(
    private val workOrderRepository: WorkOrderRepository
) {
    fun getFilteredWorkOrderList(searchWorkOrderData: SearchWorkOrderData): Flow<List<WorkOrder>> {
        return workOrderRepository.getFilteredWorkOrderList(searchWorkOrderData)
    }
}
