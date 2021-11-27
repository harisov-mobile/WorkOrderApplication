package ru.internetcloud.workorderapplication.domain.usecase

import androidx.lifecycle.LiveData
import ru.internetcloud.workorderapplication.domain.document.WorkOrder
import ru.internetcloud.workorderapplication.domain.repository.WorkOrderRepositoryInterface

class GetWorkOrderListUseCase(private val workOrderRepository: WorkOrderRepositoryInterface) {
    fun getWorkOrderList(): LiveData<List<WorkOrder>> {
        return workOrderRepository.getWorkOrderList()
    }
}
