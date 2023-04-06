package ru.internetcloud.workorderapplication.domain.usecase.documentoperation

import androidx.lifecycle.LiveData
import ru.internetcloud.workorderapplication.domain.document.WorkOrder
import ru.internetcloud.workorderapplication.domain.repository.WorkOrderRepository
import javax.inject.Inject

class GetWorkOrderListUseCase @Inject constructor(
    private val workOrderRepository: WorkOrderRepository
) {
    fun getWorkOrderList(): LiveData<List<WorkOrder>> {
        return workOrderRepository.getWorkOrderList()
    }
}
