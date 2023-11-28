package ru.internetcloud.workorderapplication.presentation.workorder.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.internetcloud.workorderapplication.domain.common.SearchWorkOrderData
import ru.internetcloud.workorderapplication.domain.model.document.WorkOrder
import ru.internetcloud.workorderapplication.domain.usecase.documentoperation.GetFilteredWorkOrderListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.documentoperation.GetWorkOrderListUseCase
import ru.internetcloud.workorderapplication.presentation.util.ReturnResult
import ru.internetcloud.workorderapplication.presentation.util.UiState

@HiltViewModel
class WorkOrderListViewModel @Inject constructor(
    private val getWorkOrderListUseCase: GetWorkOrderListUseCase,
    private val getFilteredWorkOrderListUseCase: GetFilteredWorkOrderListUseCase
) : ViewModel() {

    var scrollDownto = true

    // Сумин делает LivaData, а я более продвинутое:
    //var workOrderListLiveData: LiveData<List<WorkOrder>> = getWorkOrderListUseCase.getWorkOrderList()
    private val _screenState: MutableStateFlow<UiState<List<WorkOrder>>> = MutableStateFlow(UiState.Loading)
    val screenState = _screenState.asStateFlow()

    private var testErrorCount = 0
    var returnedResult: ReturnResult = ReturnResult.NoOperation

    var selectedWorkOrder: WorkOrder? = null
    var searchWorkOrderData: SearchWorkOrderData = SearchWorkOrderData() // для фильтра надо

    init {
        fetchWorkOrders()
    }

    fun fetchWorkOrders() {
        viewModelScope.launch {
            _screenState.value = UiState.Loading

            try {
                if (testErrorCount == 0) {
                    testErrorCount++
                    error("Some test error here!")
                }

                if (searchWorkOrderDataIsEmpty()) {
                    // workOrderListLiveData = getWorkOrderListUseCase.getWorkOrderList()
//                    getFilteredWorkOrderListUseCase.getFilteredWorkOrderList(searchWorkOrderData).collect { _ ->
//                        // ничего не делаем
//                    }
                    getWorkOrderListUseCase.getWorkOrderList().collect { list ->
                        if (list.isEmpty()) {
                            _screenState.value = UiState.EmptyData
                        } else {
                            _screenState.value = UiState.Success(
                                data = list,
                                isNew = true
                            )
                        }
                    }
                } else {
                    //workOrderListLiveData = getFilteredWorkOrderListUseCase.getFilteredWorkOrderList(searchWorkOrderData)
//                    getWorkOrderListUseCase.getWorkOrderList().collect { _ ->
//                        // ничего не делаем
//                    }
                    getFilteredWorkOrderListUseCase.getFilteredWorkOrderList(searchWorkOrderData).collect { list ->
                        if (list.isEmpty()) {
                            _screenState.value = UiState.EmptyData
                        } else {
                            _screenState.value = UiState.Success(
                                data = list,
                                isNew = true
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _screenState.value = UiState.Error(exception = e)
            }
        }
    }

    fun setIsNew(isNew: Boolean) {
        if (_screenState.value is UiState.Success) {
            _screenState.value = (_screenState.value as UiState.Success<List<WorkOrder>>).copy(
                isNew = isNew
            )
        }
    }

    fun searchWorkOrderDataIsEmpty() = searchWorkOrderData == SearchWorkOrderData()

}
