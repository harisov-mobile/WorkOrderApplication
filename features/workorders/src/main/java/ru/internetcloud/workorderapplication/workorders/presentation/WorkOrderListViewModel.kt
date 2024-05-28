package ru.internetcloud.workorderapplication.workorders.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.internetcloud.workorderapplication.common.domain.common.SearchWorkOrderData
import ru.internetcloud.workorderapplication.common.domain.model.document.WorkOrder
import ru.internetcloud.workorderapplication.common.presentation.util.ReturnResult
import ru.internetcloud.workorderapplication.common.presentation.util.UiState
import ru.internetcloud.workorderapplication.workorders.domain.usecase.GetFilteredWorkOrderListUseCase
import ru.internetcloud.workorderapplication.workorders.domain.usecase.GetWorkOrderListUseCase

@HiltViewModel
class WorkOrderListViewModel @Inject constructor(
    private val getWorkOrderListUseCase: GetWorkOrderListUseCase,
    private val getFilteredWorkOrderListUseCase: GetFilteredWorkOrderListUseCase
) : ViewModel() {

    var scrollDownto = true

    private val _screenState: MutableStateFlow<UiState<List<WorkOrder>>> = MutableStateFlow(UiState.Loading)
    val screenState = _screenState.asStateFlow()

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
                if (searchWorkOrderDataIsEmpty()) {
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
