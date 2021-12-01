package ru.internetcloud.workorderapplication.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.UUID
import ru.internetcloud.workorderapplication.domain.document.WorkOrder
import ru.internetcloud.workorderapplication.domain.repository.WorkOrderRepository

object LocalWorkOrderRepositoryImpl : WorkOrderRepository {

    private val workOrderListLiveDataMutable = MutableLiveData<List<WorkOrder>>()
    private val workOrderLiveData: LiveData<List<WorkOrder>>
    get() = workOrderListLiveDataMutable

    private val workOrderList = mutableListOf<WorkOrder>()

    init {
        for (i in 0..100) {
            val item = WorkOrder(number = "#$i")
            addWorkOrder(item)
        }
    }

    override fun addWorkOrder(workOrder: WorkOrder) {
        workOrderList.add(workOrder)
        refreshWorkOrderList()
    }

    override fun updateWorkOrder(workOrder: WorkOrder) {
        workOrderList.remove(getWorkOrder(workOrder.id))
        addWorkOrder(workOrder)
        refreshWorkOrderList()
    }

    override fun getWorkOrderList(): LiveData<List<WorkOrder>> {
        return workOrderLiveData
    }

    override fun getWorkOrder(id: UUID): WorkOrder? {
        return workOrderList.find { it.id == id }
    }

    private fun refreshWorkOrderList() {
        workOrderListLiveDataMutable.value = workOrderList.toList()
    }
}
