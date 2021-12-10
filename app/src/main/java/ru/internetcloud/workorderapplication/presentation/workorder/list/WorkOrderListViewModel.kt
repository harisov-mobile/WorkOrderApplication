package ru.internetcloud.workorderapplication.presentation.workorder.list

import androidx.lifecycle.ViewModel
import ru.internetcloud.workorderapplication.data.repository.DatabaseWorkOrderRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.LocalWorkOrderRepositoryImpl
import ru.internetcloud.workorderapplication.domain.usecase.GetWorkOrderListUseCase

class WorkOrderListViewModel : ViewModel() {

    //private val repository = LocalWorkOrderRepositoryImpl
    private val repository = DatabaseWorkOrderRepositoryImpl.get()

    private val getWorkOrderListUseCase = GetWorkOrderListUseCase(repository)

    val workOrderListLiveData = getWorkOrderListUseCase.getWorkOrderList()
}
