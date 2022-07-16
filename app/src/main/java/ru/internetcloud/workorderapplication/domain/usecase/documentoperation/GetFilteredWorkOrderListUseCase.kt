package ru.internetcloud.workorderapplication.domain.usecase.documentoperation

import androidx.lifecycle.LiveData
import javax.inject.Inject
import ru.internetcloud.workorderapplication.domain.common.SearchWorkOrderData
import ru.internetcloud.workorderapplication.domain.document.WorkOrder
import ru.internetcloud.workorderapplication.domain.repository.WorkOrderRepository

class GetFilteredWorkOrderListUseCase @Inject constructor(
    private val workOrderRepository: WorkOrderRepository
) {
    fun getFilteredWorkOrderList(searchWorkOrderData: SearchWorkOrderData): LiveData<List<WorkOrder>> {
        return workOrderRepository.getFilteredWorkOrderList(searchWorkOrderData)
    }
}
