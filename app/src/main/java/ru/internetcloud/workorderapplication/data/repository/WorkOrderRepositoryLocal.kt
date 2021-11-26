package ru.internetcloud.workorderapplication.data.repository

import ru.internetcloud.workorderapplication.domain.document.WorkOrder
import ru.internetcloud.workorderapplication.domain.repository.WorkOrderRepositoryInterface
import java.util.*

class WorkOrderRepositoryLocal: WorkOrderRepositoryInterface {

    private val workOrderList = mutableListOf<WorkOrder>()

    override fun addWorkOrder(workOrder: WorkOrder) {
        workOrderList.add(workOrder)
    }

    override fun updateWorkOrder(workOrder: WorkOrder) {
        workOrderList.remove(getWorkOrder(workOrder.id))
        addWorkOrder(workOrder)
    }

    override fun getWorkOrderList(): List<WorkOrder> {
        return workOrderList.toList()
    }

    override fun getWorkOrder(id: UUID): WorkOrder? {
        return workOrderList.find { it.id == id }
    }
}