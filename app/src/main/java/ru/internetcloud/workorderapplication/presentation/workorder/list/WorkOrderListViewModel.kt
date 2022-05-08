package ru.internetcloud.workorderapplication.presentation.workorder.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.internetcloud.workorderapplication.domain.common.SearchWorkOrderData
import ru.internetcloud.workorderapplication.domain.document.WorkOrder
import ru.internetcloud.workorderapplication.domain.usecase.documentoperation.GetFilteredWorkOrderListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.documentoperation.GetWorkOrderListUseCase
import java.util.*
import javax.inject.Inject

class WorkOrderListViewModel @Inject constructor(
    private val getWorkOrderListUseCase: GetWorkOrderListUseCase,
    private val getFilteredWorkOrderListUseCase: GetFilteredWorkOrderListUseCase
) : ViewModel() {

    var workOrderListLiveData: LiveData<List<WorkOrder>> = getWorkOrderListUseCase.getWorkOrderList()

    var selectedWorkOrder: WorkOrder? = null

    var searchWorkOrderData: SearchWorkOrderData = SearchWorkOrderData()

    fun loadWorkOrderList() {
        if (searchWorkOrderDataIsEmpty()) {
            workOrderListLiveData = getWorkOrderListUseCase.getWorkOrderList()
        } else {
            workOrderListLiveData = getFilteredWorkOrderListUseCase.getFilteredWorkOrderList(searchWorkOrderData)
        }
    }

    fun searchWorkOrderDataIsEmpty() = searchWorkOrderData == SearchWorkOrderData()

}
