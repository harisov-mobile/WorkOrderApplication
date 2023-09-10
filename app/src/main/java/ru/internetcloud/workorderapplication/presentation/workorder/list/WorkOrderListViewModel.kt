package ru.internetcloud.workorderapplication.presentation.workorder.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.internetcloud.workorderapplication.domain.common.SearchWorkOrderData
import ru.internetcloud.workorderapplication.domain.model.document.WorkOrder
import ru.internetcloud.workorderapplication.domain.usecase.documentoperation.GetFilteredWorkOrderListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.documentoperation.GetWorkOrderListUseCase
import javax.inject.Inject

class WorkOrderListViewModel @Inject constructor(
    private val getWorkOrderListUseCase: GetWorkOrderListUseCase,
    private val getFilteredWorkOrderListUseCase: GetFilteredWorkOrderListUseCase
) : ViewModel() {

    var scrollDownto = true

    var workOrderListLiveData: LiveData<List<WorkOrder>> = getWorkOrderListUseCase.getWorkOrderList()

    var selectedWorkOrder: WorkOrder? = null

    var searchWorkOrderData: SearchWorkOrderData = SearchWorkOrderData()

    var newWorkOrderId: String? = null

    fun loadWorkOrderList() {
        if (searchWorkOrderDataIsEmpty()) {
            workOrderListLiveData = getWorkOrderListUseCase.getWorkOrderList()
        } else {
            workOrderListLiveData = getFilteredWorkOrderListUseCase.getFilteredWorkOrderList(searchWorkOrderData)
        }
    }

    fun searchWorkOrderDataIsEmpty() = searchWorkOrderData == SearchWorkOrderData()

    fun getPosition(workOrderId: String): Int? {
        var currentPosition = NOT_FOUND_POSITION
        var isFound = false

        workOrderListLiveData.value?.let { list ->
            for (currentOrder in list) {
                currentPosition++
                if (currentOrder.id == workOrderId) {
                    isFound = true
                    break
                }
            }
        }

        return if (isFound) {
            currentPosition
        } else {
            null
        }
    }
    companion object {
        const val NOT_FOUND_POSITION = -1
    }
}
