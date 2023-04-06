package ru.internetcloud.workorderapplication.presentation.workorder.detail.workinghour

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.internetcloud.workorderapplication.domain.catalog.WorkingHour
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.workinghour.GetWorkingHourListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.workinghour.SearchWorkingHoursUseCase
import javax.inject.Inject

class WorkingHourListViewModel @Inject constructor(
    private val getWorkingHourListUseCase: GetWorkingHourListUseCase,
    private val searchWorkingHoursUseCase: SearchWorkingHoursUseCase
) : ViewModel() {

    var selectedWorkingHour: WorkingHour? = null

    // private val repository = DbWorkingHourRepositoryImpl.get()

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
