package ru.internetcloud.workorderapplication.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.internetcloud.workorderapplication.data.repository.WorkOrderRepositoryLocal
import ru.internetcloud.workorderapplication.domain.document.WorkOrder
import ru.internetcloud.workorderapplication.domain.usecase.GetWorkOrderListUseCase

class WorkOrderListViewModel : ViewModel() {

    private val repository = WorkOrderRepositoryLocal

    private val getWorkOrderListUseCase = GetWorkOrderListUseCase(repository)

    private val workOrderListMutableLiveData = MutableLiveData<List<WorkOrder>>()

    val workOrderListLiveData: LiveData<List<WorkOrder>>
        get() = workOrderListMutableLiveData

    fun getWorkOrderList() {
        workOrderListMutableLiveData.value = getWorkOrderListUseCase.getWorkOrderList()
    }
}
