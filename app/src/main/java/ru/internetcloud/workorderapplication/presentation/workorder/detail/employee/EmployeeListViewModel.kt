package ru.internetcloud.workorderapplication.presentation.workorder.detail.employee

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import ru.internetcloud.workorderapplication.domain.model.catalog.Employee
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.employee.GetEmployeeListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.employee.SearchEmployeesUseCase

@HiltViewModel
class EmployeeListViewModel @Inject constructor(
    private val getEmployeeListUseCase: GetEmployeeListUseCase,
    private val searchEmployeesUseCase: SearchEmployeesUseCase
) : ViewModel() {

    var selectedEmployee: Employee? = null

    private val _employeeListLiveData = MutableLiveData<List<Employee>>()
    val employeeListLiveData: LiveData<List<Employee>>
        get() = _employeeListLiveData

    fun loadEmployeeList() {
        viewModelScope.launch {
            _employeeListLiveData.value = getEmployeeListUseCase.getEmployeeList()
        }
    }

    fun searchEmployees(searchText: String) {
        viewModelScope.launch {
            _employeeListLiveData.value = searchEmployeesUseCase.searchEmployees(searchText)
        }
    }
}
