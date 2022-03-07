package ru.internetcloud.workorderapplication.presentation.workorder.detail.department

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.internetcloud.workorderapplication.di.qualifiers.usecase.DbGetDepartmentListUseCaseQualifier
import ru.internetcloud.workorderapplication.domain.catalog.Department
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.department.GetDepartmentListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.department.SearchDepartmentsUseCase
import javax.inject.Inject

class DepartmentListViewModel @Inject constructor(
    @DbGetDepartmentListUseCaseQualifier
    private val getDepartmentListUseCase: GetDepartmentListUseCase,

    private val searchDepartmentsUseCase: SearchDepartmentsUseCase
) : ViewModel() {

    var selectedDepartment: Department? = null

    // private val repository = DbDepartmentRepositoryImpl.get()

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
