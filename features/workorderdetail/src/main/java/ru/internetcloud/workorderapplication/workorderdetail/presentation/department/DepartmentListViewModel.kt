package ru.internetcloud.workorderapplication.workorderdetail.presentation.department

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import ru.internetcloud.workorderapplication.common.domain.model.catalog.Department
import ru.internetcloud.workorderapplication.common.domain.usecase.catalogoperation.department.GetDepartmentListUseCase
import ru.internetcloud.workorderapplication.common.domain.usecase.catalogoperation.department.SearchDepartmentsUseCase

@HiltViewModel
class DepartmentListViewModel @Inject constructor(
    private val getDepartmentListUseCase: GetDepartmentListUseCase,
    private val searchDepartmentsUseCase: SearchDepartmentsUseCase
) : ViewModel() {

    var selectedDepartment: Department? = null

    private val _departmentListLiveData = MutableLiveData<List<Department>>()
    val departmentListLiveData: LiveData<List<Department>>
        get() = _departmentListLiveData

    fun loadDepartmentList() {
        viewModelScope.launch {
            _departmentListLiveData.value = getDepartmentListUseCase.getDepartmentList()
        }
    }

    fun searchDepartments(searchText: String) {
        viewModelScope.launch {
            _departmentListLiveData.value = searchDepartmentsUseCase.searchDepartments(searchText)
        }
    }
}
