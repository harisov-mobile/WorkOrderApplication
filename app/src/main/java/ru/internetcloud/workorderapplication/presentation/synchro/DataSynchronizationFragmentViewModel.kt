package ru.internetcloud.workorderapplication.presentation.synchro

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.internetcloud.workorderapplication.data.repository.*
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.carjob.AddCarJobListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.carjob.AddCarJobUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.carjob.DeleteCarJobsUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.carjob.GetCarJobListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.department.AddDepartmentUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.department.DeleteDepartmentsUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.department.GetDepartmentListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.employee.AddEmployeeListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.employee.AddEmployeeUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.employee.DeleteEmployeesUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.employee.GetEmployeeListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.repairtype.AddRepairTypeUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.repairtype.DeleteRepairTypesUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.repairtype.GetRepairTypeListUseCase

class DataSynchronizationFragmentViewModel : ViewModel() {

    private val remoteRepairTypeRepository = RemoteRepairTypeRepositoryImpl.get() // требуется инъекция зависимостей!!!
    private val dbRepairTypeRepository = DbRepairTypeRepositoryImpl.get() // требуется инъекция зависимостей!!!

    private val remoteCarJobRepository = RemoteCarJobRepositoryImpl.get() // требуется инъекция зависимостей!!!
    private val dbCarJobRepository = DbCarJobRepositoryImpl.get() // требуется инъекция зависимостей!!!

    private val remoteDepartmentRepository = RemoteDepartmentRepositoryImpl.get() // требуется инъекция зависимостей!!!
    private val dbDepartmentRepository = DbDepartmentRepositoryImpl.get() // требуется инъекция зависимостей!!!

    private val remoteEmployeeRepository = RemoteEmployeeRepositoryImpl.get() // требуется инъекция зависимостей!!!
    private val dbEmployeeRepository = DbEmployeeRepositoryImpl.get() // требуется инъекция зависимостей!!!

    // ссылки на экземпляры классов Юзе-Кейсов, которые будут использоваться в Вью-Модели:
    private val getRemoteRepairTypeListUseCase = GetRepairTypeListUseCase(remoteRepairTypeRepository)
    private val getDbRepairTypeListUseCase = GetRepairTypeListUseCase(dbRepairTypeRepository)
    private val addDbRepairTypeUseCase = AddRepairTypeUseCase(dbRepairTypeRepository)
    private val deleteRepairTypesUseCase = DeleteRepairTypesUseCase(dbRepairTypeRepository)

    private val getRemoteCarJobListUseCase = GetCarJobListUseCase(remoteCarJobRepository)
    private val getDbCarJobListUseCase = GetCarJobListUseCase(dbCarJobRepository)
    private val addDbCarJobUseCase = AddCarJobUseCase(dbCarJobRepository)
    private val addDbCarJobListUseCase = AddCarJobListUseCase(dbCarJobRepository)
    private val deleteCarJobsUseCase = DeleteCarJobsUseCase(dbCarJobRepository)

    private val getRemoteDepartmentListUseCase = GetDepartmentListUseCase(remoteDepartmentRepository)
    private val getDbDepartmentListUseCase = GetDepartmentListUseCase(dbDepartmentRepository)
    private val addDbDepartmentUseCase = AddDepartmentUseCase(dbDepartmentRepository)
    private val deleteDepartmentsUseCase = DeleteDepartmentsUseCase(dbDepartmentRepository)

    private val getRemoteEmployeeListUseCase = GetEmployeeListUseCase(remoteEmployeeRepository)
    private val getDbEmployeeListUseCase = GetEmployeeListUseCase(dbEmployeeRepository)
    private val addDbEmployeeUseCase = AddEmployeeUseCase(dbEmployeeRepository)
    private val addDbEmployeeListUseCase = AddEmployeeListUseCase(dbEmployeeRepository)
    private val deleteEmployeesUseCase = DeleteEmployeesUseCase(dbEmployeeRepository)

    private val _canContinue = MutableLiveData<Boolean>()
    val canContinue: LiveData<Boolean>
        get() = _canContinue

    private val _canContinueWithoutSynchro = MutableLiveData<Boolean>()
    val canContinueWithoutSynchro: LiveData<Boolean>
        get() = _canContinueWithoutSynchro

    private val _errorSynchronization = MutableLiveData<Boolean>()
    val errorSynchronization: LiveData<Boolean>
        get() = _errorSynchronization

    private val _currentSituation = MutableLiveData<String>()
    val currentSituation: LiveData<String>
        get() = _currentSituation

    fun synchonizeData() {
        viewModelScope.launch {
            val remoteRepairTypeList = getRemoteRepairTypeListUseCase.getRepairTypeList()

            Log.i("rustam", " remoteRepairTypeList = " + remoteRepairTypeList.toString())

            if (remoteRepairTypeList.isEmpty()) {
                // не удалось получить данные из сервера 1С, надо проверить, есть ли данные в БД
                val dbRepairTypeList = getDbRepairTypeListUseCase.getRepairTypeList()
                Log.i("rustam", " dbRepairTypeList = " + dbRepairTypeList.toString())

                if (dbRepairTypeList.isEmpty()) {
                    _errorSynchronization.value = true
                } else {
                    _canContinueWithoutSynchro.value = true
                }
            } else {
                deleteRepairTypesUseCase.deleteAllRepairTypes()
                remoteRepairTypeList.forEach {
                    addDbRepairTypeUseCase.addRepairType(it)
                }

                refreshCarJob()
                refreshDepartment()
                refreshEmployee()

                _currentSituation.value = ""
                _canContinue.value = true
            }
        }
    }

    suspend fun refreshCarJob() {
        _currentSituation.value = "Получение справочника Автоработы из 1С"
        val remoteCarJobList = getRemoteCarJobListUseCase.getCarJobList()
        if (!remoteCarJobList.isEmpty()) {
            deleteCarJobsUseCase.deleteAllCarJobs()
            _currentSituation.value = "Обработка справочника Автоработы"
            addDbCarJobListUseCase.addCarJobList(remoteCarJobList)
        }
    }

    suspend fun refreshDepartment() {
        _currentSituation.value = "Получение справочника Цеха из 1С"
        val remoteDepartmentList = getRemoteDepartmentListUseCase.getDepartmentList()
        if (!remoteDepartmentList.isEmpty()) {
            deleteDepartmentsUseCase.deleteAllDepartments()
            _currentSituation.value = "Обработка справочника Цеха"
            remoteDepartmentList.forEach {
                addDbDepartmentUseCase.addDepartment(it)
            }
        }
    }

    suspend fun refreshEmployee() {
        _currentSituation.value = "Получение справочника Сотрудники из 1С"
        val remoteEmployeeList = getRemoteEmployeeListUseCase.getEmployeeList()
        if (!remoteEmployeeList.isEmpty()) {
            deleteEmployeesUseCase.deleteAllEmployees()
            _currentSituation.value = "Обработка справочника Сотрудники"
            addDbEmployeeListUseCase.addEmployeeList(remoteEmployeeList)
        }
    }
}
