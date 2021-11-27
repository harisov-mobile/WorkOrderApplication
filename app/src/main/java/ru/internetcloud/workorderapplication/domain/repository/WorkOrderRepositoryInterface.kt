package ru.internetcloud.workorderapplication.domain.repository

import androidx.lifecycle.LiveData
import java.util.UUID
import ru.internetcloud.workorderapplication.domain.document.WorkOrder

interface WorkOrderRepositoryInterface {
    fun addWorkOrder(workOrder: WorkOrder)

    fun updateWorkOrder(workOrder: WorkOrder)

    fun getWorkOrderList(): LiveData<List<WorkOrder>>

    fun getWorkOrder(id: UUID): WorkOrder?
}
