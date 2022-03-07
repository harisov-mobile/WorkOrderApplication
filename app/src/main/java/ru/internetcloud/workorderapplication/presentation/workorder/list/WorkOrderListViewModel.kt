package ru.internetcloud.workorderapplication.presentation.workorder.list

import androidx.lifecycle.ViewModel
import ru.internetcloud.workorderapplication.domain.usecase.documentoperation.GetWorkOrderListUseCase
import javax.inject.Inject

class WorkOrderListViewModel @Inject constructor(
    private val getWorkOrderListUseCase: GetWorkOrderListUseCase
) : ViewModel() {

    val workOrderListLiveData = getWorkOrderListUseCase.getWorkOrderList()
}
