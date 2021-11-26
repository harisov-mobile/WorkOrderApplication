package ru.internetcloud.workorderapplication.domain.usecase

import ru.internetcloud.workorderapplication.domain.document.WorkOrder
import ru.internetcloud.workorderapplication.domain.repository.WorkOrderRepositoryInterface

class GetWorkOrderListUseCase(private val workOrderRepository: WorkOrderRepositoryInterface) {
    fun getWorkOrderList() : List<WorkOrder> {
        return workOrderRepository.getWorkOrderList()
    }
}
