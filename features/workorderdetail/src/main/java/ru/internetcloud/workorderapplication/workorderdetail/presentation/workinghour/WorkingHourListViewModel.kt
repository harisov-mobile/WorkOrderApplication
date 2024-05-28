package ru.internetcloud.workorderapplication.workorderdetail.presentation.workinghour

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import ru.internetcloud.workorderapplication.common.domain.model.catalog.WorkingHour
import ru.internetcloud.workorderapplication.common.domain.usecase.catalogoperation.workinghour.GetWorkingHourListUseCase
import ru.internetcloud.workorderapplication.common.domain.usecase.catalogoperation.workinghour.SearchWorkingHoursUseCase

@HiltViewModel
class WorkingHourListViewModel @Inject constructor(
    private val getWorkingHourListUseCase: GetWorkingHourListUseCase,
    private val searchWorkingHoursUseCase: SearchWorkingHoursUseCase
) : ViewModel() {

    var selectedWorkingHour: WorkingHour? = null

    private val _workingHourListLiveData = MutableLiveData<List<WorkingHour>>()
    val workingHourListLiveData: LiveData<List<WorkingHour>>
        get() = _workingHourListLiveData

    fun loadWorkingHourList() {
        viewModelScope.launch {
            _workingHourListLiveData.value = getWorkingHourListUseCase.getWorkingHourList()
        }
    }

    fun searchWorkingHours(searchText: String) {
        viewModelScope.launch {
            _workingHourListLiveData.value = searchWorkingHoursUseCase.searchWorkingHours(searchText)
        }
    }
}
