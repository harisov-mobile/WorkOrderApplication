package ru.internetcloud.workorderapplication.presentation

import androidx.lifecycle.ViewModel
import ru.internetcloud.workorderapplication.data.repository.WorkOrderRepositoryLocal
import ru.internetcloud.workorderapplication.domain.usecase.GetWorkOrderListUseCase

class WorkOrderListViewModel : ViewModel() {

    private val repository = WorkOrderRepositoryLocal

    private val getWorkOrderListUseCase = GetWorkOrderListUseCase(repository)

    val workOrderListLiveData = getWorkOrderListUseCase.getWorkOrderList()
}
