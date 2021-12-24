package ru.internetcloud.workorderapplication.presentation.synchro

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.internetcloud.workorderapplication.data.repository.*
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.car.AddCarListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.car.DeleteCarListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.car.GetCarListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.carjob.AddCarJobListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.carjob.DeleteCarJobsUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.carjob.GetCarJobListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.department.AddDepartmentUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.department.DeleteDepartmentsUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.department.GetDepartmentListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.employee.AddEmployeeListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.employee.DeleteEmployeesUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.employee.GetEmployeeListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.partner.AddPartnerListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.partner.DeletePartnerListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.partner.GetPartnerListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.repairtype.AddRepairTypeUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.repairtype.DeleteRepairTypesUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.repairtype.GetRepairTypeListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.workinghour.AddWorkingHourListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.workinghour.DeleteWorkingHourListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.workinghour.GetWorkingHourListUseCase

class DataSynchronizationFragmentViewModel : ViewModel() {

    private val remoteRepairTypeRepository = RemoteRepairTypeRepositoryImpl.get() // требуется инъекция зависимостей!!!
    private val dbRepairTypeRepository = DbRepairTypeRepositoryImpl.get() // требуется инъекция зависимостей!!!

    private val remoteCarJobRepository = RemoteCarJobRepositoryImpl.get() // требуется инъекция зависимостей!!!
    private val dbCarJobRepository = DbCarJobRepositoryImpl.get() // требуется инъекция зависимостей!!!

    private val remoteDepartmentRepository = RemoteDepartmentRepositoryImpl.get() // требуется инъекция зависимостей!!!
    private val dbDepartmentRepository = DbDepartmentRepositoryImpl.get() // требуется инъекция зависимостей!!!

    private val remoteEmployeeRepository = RemoteEmployeeRepositoryImpl.get() // требуется инъекция зависимостей!!!
    private val dbEmployeeRepository = DbEmployeeRepositoryImpl.get() // требуется инъекция зависимостей!!!

    private val remotePartnerRepository = RemotePartnerRepositoryImpl.get() // требуется инъекция зависимостей!!!
    private val dbPartnerRepository = DbPartnerRepositoryImpl.get() // требуется инъекция зависимостей!!!

    private val remoteCarRepository = RemoteCarRepositoryImpl.get() // требуется инъекция зависимостей!!!
    private val dbCarRepository = DbCarRepositoryImpl.get() // требуется инъекция зависимостей!!!

    private val remoteWorkingHourRepository = RemoteWorkingHourRepositoryImpl.get() // требуется инъекция зависимостей!!!
    private val dbWorkingHourRepository = DbWorkingHourRepositoryImpl.get() // требуется инъекция зависимостей!!!

    private val loadWorkOrderRepositoryImpl = LoadWorkOrderRepositoryImpl.get() // требуется инъекция зависимостей!!!

    // ссылки на экземпляры классов Юзе-Кейсов, которые будут использоваться в Вью-Модели:
    private val getRemoteRepairTypeListUseCase = GetRepairTypeListUseCase(remoteRepairTypeRepository)
    private val getDbRepairTypeListUseCase = GetRepairTypeListUseCase(dbRepairTypeRepository)
    private val addDbRepairTypeUseCase = AddRepairTypeUseCase(dbRepairTypeRepository)
    private val deleteRepairTypesUseCase = DeleteRepairTypesUseCase(dbRepairTypeRepository)

    private val getRemoteCarJobListUseCase = GetCarJobListUseCase(remoteCarJobRepository)
    private val addDbCarJobListUseCase = AddCarJobListUseCase(dbCarJobRepository)
    private val deleteCarJobsUseCase = DeleteCarJobsUseCase(dbCarJobRepository)

    private val getRemoteDepartmentListUseCase = GetDepartmentListUseCase(remoteDepartmentRepository)
    private val addDbDepartmentUseCase = AddDepartmentUseCase(dbDepartmentRepository)
    private val deleteDepartmentsUseCase = DeleteDepartmentsUseCase(dbDepartmentRepository)

    private val getRemoteEmployeeListUseCase = GetEmployeeListUseCase(remoteEmployeeRepository)
    private val addDbEmployeeListUseCase = AddEmployeeListUseCase(dbEmployeeRepository)
    private val deleteEmployeesUseCase = DeleteEmployeesUseCase(dbEmployeeRepository)

    private val getRemotePartnerListUseCase = GetPartnerListUseCase(remotePartnerRepository)
    private val addDbPartnerListUseCase = AddPartnerListUseCase(dbPartnerRepository)
    private val deletePartnersUseCase = DeletePartnerListUseCase(dbPartnerRepository)

    private val getRemoteCarListUseCase = GetCarListUseCase(remoteCarRepository)
    private val addDbCarListUseCase = AddCarListUseCase(dbCarRepository)
    private val deleteCarsUseCase = DeleteCarListUseCase(dbCarRepository)

    private val getRemoteWorkingHourListUseCase = GetWorkingHourListUseCase(remoteWorkingHourRepository)
    private val addDbWorkingHourListUseCase = AddWorkingHourListUseCase(dbWorkingHourRepository)
    private val deleteWorkingHoursUseCase = DeleteWorkingHourListUseCase(dbWorkingHourRepository)

//    private val getRemoteWorkOrderListUseCase = GetWorkOrderListUseCase(remoteWorkOrderRepository)
//    private val getDbWorkOrderListUseCase = GetWorkOrderListUseCase(dbWorkOrderRepository)
//    private val addDbWorkOrderListUseCase = AddWorkOrderListUseCase(dbWorkOrderRepository)
//    private val deleteWorkOrdersUseCase = DeleteWorkOrderListUseCase(dbWorkOrderRepository)

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

            // послать новые или измененные заказ-наряды в 1С
            // success = uploadWorkOrders()...
            // if (success) {

            val remoteRepairTypeList = getRemoteRepairTypeListUseCase.getRepairTypeList()

            if (remoteRepairTypeList.isEmpty()) {
                // не удалось получить данные из сервера 1С, надо проверить, есть ли данные в БД
                val dbRepairTypeList = getDbRepairTypeListUseCase.getRepairTypeList()

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

                refreshPartner()
                refreshEmployee()
                refreshCarJob()
                refreshCar()
                refreshDepartment()
                refreshWorkingHour()
                refreshWorkOrder()

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

    suspend fun refreshPartner() {
        _currentSituation.value = "Получение справочника Контрагенты из 1С"
        val remotePartnerList = getRemotePartnerListUseCase.getPartnerList()
        if (!remotePartnerList.isEmpty()) {
            deletePartnersUseCase.deleteAllPartners()
            _currentSituation.value = "Обработка справочника Контрагенты"
            addDbPartnerListUseCase.addPartnerList(remotePartnerList)
        }
    }

    suspend fun refreshCar() {
        _currentSituation.value = "Получение справочника СХТ из 1С"
        Log.i("rustam", "Получение справочника СХТ из 1С")
        val remoteCarList = getRemoteCarListUseCase.getCarList()
        if (!remoteCarList.isEmpty()) {
            deleteCarsUseCase.deleteAllCars()
            _currentSituation.value = "Обработка справочника СХТ"
            addDbCarListUseCase.addCarList(remoteCarList)
        }
    }

    suspend fun refreshWorkingHour() {
        _currentSituation.value = "Получение справочника Нормочасы из 1С"
        val remoteWorkingHourList = getRemoteWorkingHourListUseCase.getWorkingHourList()
        if (!remoteWorkingHourList.isEmpty()) {
            deleteWorkingHoursUseCase.deleteAllWorkingHours()
            _currentSituation.value = "Обработка справочника Нормочасы"
            addDbWorkingHourListUseCase.addWorkingHourList(remoteWorkingHourList)
        }
    }

    suspend fun refreshWorkOrder() {
        _currentSituation.value = "Получение документов Заказ-наряд из 1С"
        val success = loadWorkOrderRepositoryImpl.loadWorkOrderList()
        Log.i("rustam", "success = " + success.toString())
    }
}
