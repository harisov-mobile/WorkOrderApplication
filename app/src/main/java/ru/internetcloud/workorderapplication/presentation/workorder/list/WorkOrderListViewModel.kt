package ru.internetcloud.workorderapplication.presentation.workorder.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.internetcloud.workorderapplication.data.repository.DbWorkOrderRepositoryImpl
import ru.internetcloud.workorderapplication.domain.document.WorkOrder
import ru.internetcloud.workorderapplication.domain.usecase.documentoperation.GetWorkOrderListUseCase

class WorkOrderListViewModel : ViewModel() {

    private val repository = DbWorkOrderRepositoryImpl.get()

    private val getWorkOrderListUseCase = GetWorkOrderListUseCase(repository)

    val workOrderListLiveData = getWorkOrderListUseCase.getWorkOrderList()

}
