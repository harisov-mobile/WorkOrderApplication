package ru.internetcloud.workorderapplication.data.repository

// object LocalWorkOrderRepositoryImpl : WorkOrderRepository {
object LocalWorkOrderRepositoryImpl {

//    private val workOrderListLiveDataMutable = MutableLiveData<List<WorkOrder>>()
//    private val workOrderLiveData: LiveData<List<WorkOrder>>
//    get() = workOrderListLiveDataMutable
//
//    private val workOrderList = mutableListOf<WorkOrder>()
//
//    init {
//        for (i in 0..4) {
//            val item = WorkOrder(id = i, number = "#$i", id1C = i.toString())
//            addWorkOrder(item)
//        }
//    }
//
//    override fun addWorkOrder(workOrder: WorkOrder) {
//        workOrderList.add(workOrder)
//        refreshWorkOrderList()
//    }
//
//    override fun updateWorkOrder(workOrder: WorkOrder) {
//        workOrderList.remove(getWorkOrder(workOrder.id))
//        addWorkOrder(workOrder)
//        refreshWorkOrderList()
//    }
//
//    override fun getWorkOrderList(): LiveData<List<WorkOrder>> {
//        return workOrderLiveData
//    }
//
//    override fun getWorkOrder(id: Int): WorkOrder? {
//        return workOrderList.find { it.id == id }
//    }
//
//    private fun refreshWorkOrderList() {
//        workOrderListLiveDataMutable.value = workOrderList.toList()
//    }
}
