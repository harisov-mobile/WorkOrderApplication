package ru.internetcloud.workorderapplication.domain.usecase.documentoperation

import androidx.lifecycle.LiveData
import ru.internetcloud.workorderapplication.domain.common.SearchWorkOrderData
import ru.internetcloud.workorderapplication.domain.model.document.WorkOrder
import ru.internetcloud.workorderapplication.domain.repository.WorkOrderRepository
import javax.inject.Inject

class GetFilteredWorkOrderListUseCase @Inject constructor(
    private val workOrderRepository: WorkOrderRepository
) {
    fun getFilteredWorkOrderList(searchWorkOrderData: SearchWorkOrderData): LiveData<List<WorkOrder>> {
        return workOrderRepository.getFilteredWorkOrderList(searchWorkOrderData)
    }
}
