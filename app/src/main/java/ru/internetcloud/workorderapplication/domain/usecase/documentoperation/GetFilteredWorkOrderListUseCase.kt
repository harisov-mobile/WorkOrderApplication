package ru.internetcloud.workorderapplication.domain.usecase.documentoperation

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import ru.internetcloud.workorderapplication.domain.common.SearchWorkOrderData
import ru.internetcloud.workorderapplication.domain.model.document.WorkOrder
import ru.internetcloud.workorderapplication.domain.repository.WorkOrderRepository

class GetFilteredWorkOrderListUseCase @Inject constructor(
    private val workOrderRepository: WorkOrderRepository
) {
    fun getFilteredWorkOrderList(searchWorkOrderData: SearchWorkOrderData): Flow<List<WorkOrder>> {
        return workOrderRepository.getFilteredWorkOrderList(searchWorkOrderData)
    }
}
