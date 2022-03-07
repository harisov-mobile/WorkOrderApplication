package ru.internetcloud.workorderapplication.presentation.synchro

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.internetcloud.workorderapplication.di.qualifiers.usecase.*
import ru.internetcloud.workorderapplication.domain.common.FunctionResult
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
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.repairtype.AddRepairTypeListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.repairtype.DeleteRepairTypesUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.repairtype.GetRepairTypeListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.workinghour.AddWorkingHourListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.workinghour.DeleteWorkingHourListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.workinghour.GetWorkingHourListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.synchrooperation.GetModifiedWorkOrdersQuantityUseCase
import ru.internetcloud.workorderapplication.domain.usecase.synchrooperation.LoadDefaultWorkOrderSettingsUseCase
import ru.internetcloud.workorderapplication.domain.usecase.synchrooperation.LoadWorkOrdersUseCase
import ru.internetcloud.workorderapplication.domain.usecase.synchrooperation.UploadWorkOrdersUseCase
import javax.inject.Inject

class DataSynchronizationFragmentViewModel @Inject constructor(
    @RemoteGetRepairTypeListUseCaseQualifier
    private val getRemoteRepairTypeListUseCase: GetRepairTypeListUseCase,

    @DbGetRepairTypeListUseCaseQualifier
    private val getDbRepairTypeListUseCase: GetRepairTypeListUseCase,

    private val addRepairTypeListUseCase: AddRepairTypeListUseCase,
    private val deleteRepairTypesUseCase: DeleteRepairTypesUseCase,

    @RemoteGetCarJobListUseCaseQualifier
    private val getRemoteCarJobListUseCase: GetCarJobListUseCase,

    private val addDbCarJobListUseCase: AddCarJobListUseCase,
    private val deleteCarJobsUseCase: DeleteCarJobsUseCase,

    @RemoteGetDepartmentListUseCaseQualifier
    private val getRemoteDepartmentListUseCase: GetDepartmentListUseCase,

    private val addDbDepartmentUseCase: AddDepartmentUseCase,
    private val deleteDepartmentsUseCase: DeleteDepartmentsUseCase,

    @RemoteGetEmployeeListUseCaseQualifier
    private val getRemoteEmployeeListUseCase: GetEmployeeListUseCase,
    private val addDbEmployeeListUseCase: AddEmployeeListUseCase,
    private val deleteEmployeesUseCase: DeleteEmployeesUseCase,

    @RemoteGetPartnerListUseCaseQualifier
    private val getRemotePartnerListUseCase: GetPartnerListUseCase,
    private val addDbPartnerListUseCase: AddPartnerListUseCase,
    private val deletePartnersUseCase: DeletePartnerListUseCase,

    @RemoteGetCarListUseCaseQualifier
    private val getRemoteCarListUseCase: GetCarListUseCase,
    private val addDbCarListUseCase: AddCarListUseCase,
    private val deleteCarsUseCase: DeleteCarListUseCase,

    @RemoteGetWorkingHourListUseCaseQualifier
    private val getRemoteWorkingHourListUseCase: GetWorkingHourListUseCase,
    private val addDbWorkingHourListUseCase: AddWorkingHourListUseCase,
    private val deleteWorkingHoursUseCase: DeleteWorkingHourListUseCase,

    private val getModifiedWorkOrdersQuantityUseCase: GetModifiedWorkOrdersQuantityUseCase,
    private val uploadWorkOrdersUseCase: UploadWorkOrdersUseCase,
    private val loadWorkOrdersUseCase: LoadWorkOrdersUseCase,

    private val loadDefaultWorkOrderSettingsUseCase: LoadDefaultWorkOrderSettingsUseCase

) : ViewModel() {

    // Репозитории
//    private val remoteRepairTypeRepository = RemoteRepairTypeRepositoryImpl.get() // требуется инъекция зависимостей!!!
//    private val dbRepairTypeRepository = DbRepairTypeRepositoryImpl.get() // требуется инъекция зависимостей!!!
//
//    private val remoteCarJobRepository = RemoteCarJobRepositoryImpl.get() // требуется инъекция зависимостей!!!
//    private val dbCarJobRepository = DbCarJobRepositoryImpl.get() // требуется инъекция зависимостей!!!
//
//    private val remoteDepartmentRepository = RemoteDepartmentRepositoryImpl.get() // требуется инъекция зависимостей!!!
//    private val dbDepartmentRepository = DbDepartmentRepositoryImpl.get() // требуется инъекция зависимостей!!!
//
//    private val remoteEmployeeRepository = RemoteEmployeeRepositoryImpl.get() // требуется инъекция зависимостей!!!
//    private val dbEmployeeRepository = DbEmployeeRepositoryImpl.get() // требуется инъекция зависимостей!!!
//
//    private val remotePartnerRepository = RemotePartnerRepositoryImpl.get() // требуется инъекция зависимостей!!!
//    private val dbPartnerRepository = DbPartnerRepositoryImpl.get() // требуется инъекция зависимостей!!!
//
//    private val remoteCarRepository = RemoteCarRepositoryImpl.get() // требуется инъекция зависимостей!!!
//    private val dbCarRepository = DbCarRepositoryImpl.get() // требуется инъекция зависимостей!!!
//
//    private val remoteWorkingHourRepository = RemoteWorkingHourRepositoryImpl.get() // требуется инъекция зависимостей!!!
//    private val dbWorkingHourRepository = DbWorkingHourRepositoryImpl.get() // требуется инъекция зависимостей!!!
//
//    private val synchroRepositoryImpl = SynchroRepositoryImpl.get() // требуется инъекция зависимостей!!!

    // ссылки на экземпляры классов Юзе-Кейсов, которые будут использоваться в Вью-Модели:
//    private val getRemoteRepairTypeListUseCase = GetRepairTypeListUseCase(remoteRepairTypeRepository)
//    private val getDbRepairTypeListUseCase = GetRepairTypeListUseCase(dbRepairTypeRepository)
//    private val addRepairTypeListUseCase = AddRepairTypeListUseCase(dbRepairTypeRepository)
//    private val deleteRepairTypesUseCase = DeleteRepairTypesUseCase(dbRepairTypeRepository)
//
//    private val getRemoteCarJobListUseCase = GetCarJobListUseCase(remoteCarJobRepository)
//    private val addDbCarJobListUseCase = AddCarJobListUseCase(dbCarJobRepository)
//    private val deleteCarJobsUseCase = DeleteCarJobsUseCase(dbCarJobRepository)
//
//    private val getRemoteDepartmentListUseCase = GetDepartmentListUseCase(remoteDepartmentRepository)
//    private val addDbDepartmentUseCase = AddDepartmentUseCase(dbDepartmentRepository)
//    private val deleteDepartmentsUseCase = DeleteDepartmentsUseCase(dbDepartmentRepository)
//
//    private val getRemoteEmployeeListUseCase = GetEmployeeListUseCase(remoteEmployeeRepository)
//    private val addDbEmployeeListUseCase = AddEmployeeListUseCase(dbEmployeeRepository)
//    private val deleteEmployeesUseCase = DeleteEmployeesUseCase(dbEmployeeRepository)
//
//    private val getRemotePartnerListUseCase = GetPartnerListUseCase(remotePartnerRepository)
//    private val addDbPartnerListUseCase = AddPartnerListUseCase(dbPartnerRepository)
//    private val deletePartnersUseCase = DeletePartnerListUseCase(dbPartnerRepository)
//
//    private val getRemoteCarListUseCase = GetCarListUseCase(remoteCarRepository)
//    private val addDbCarListUseCase = AddCarListUseCase(dbCarRepository)
//    private val deleteCarsUseCase = DeleteCarListUseCase(dbCarRepository)
//
//    private val getRemoteWorkingHourListUseCase = GetWorkingHourListUseCase(remoteWorkingHourRepository)
//    private val addDbWorkingHourListUseCase = AddWorkingHourListUseCase(dbWorkingHourRepository)
//    private val deleteWorkingHoursUseCase = DeleteWorkingHourListUseCase(dbWorkingHourRepository)
//
//    private val getModifiedWorkOrdersQuantityUseCase = GetModifiedWorkOrdersQuantityUseCase(synchroRepositoryImpl)
//    private val uploadWorkOrdersUseCase = UploadWorkOrdersUseCase(synchroRepositoryImpl)
//    private val loadWorkOrdersUseCase = LoadWorkOrdersUseCase(synchroRepositoryImpl)
//
//    private val loadDefaultWorkOrderSettingsUseCase = LoadDefaultWorkOrderSettingsUseCase(synchroRepositoryImpl)

    // ЛивДаты
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

    private val _uploadResult = MutableLiveData<FunctionResult>()
    val uploadResult: LiveData<FunctionResult>
        get() = _uploadResult

    fun synchonizeData() {
        viewModelScope.launch {

            val quantity = getModifiedWorkOrdersQuantity()
            if (quantity > 0) {
                // послать новые или измененные заказ-наряды в 1С
                val result = uploadWorkOrders()
                result?.let {
                    _uploadResult.value = it
                }
            }

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
                addRepairTypeListUseCase.addRepairTypeList(remoteRepairTypeList)

                refreshPartner()
                refreshEmployee()
                refreshCarJob()
                refreshCar()
                refreshDepartment()
                refreshWorkingHour()
                refreshDefaultWorkOrderSettings()
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
        Log.i("rustam", "Получение справочника Цеха из 1С")
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
        Log.i("rustam", "Получение справочника Нормочасы из 1С")
        val remoteWorkingHourList = getRemoteWorkingHourListUseCase.getWorkingHourList()
        if (!remoteWorkingHourList.isEmpty()) {
            deleteWorkingHoursUseCase.deleteAllWorkingHours()
            _currentSituation.value = "Обработка справочника Нормочасы"
            addDbWorkingHourListUseCase.addWorkingHourList(remoteWorkingHourList)
        }
    }

    suspend fun refreshDefaultWorkOrderSettings() {
        _currentSituation.value = "Получение настроек заполнения из 1С"
        Log.i("rustam", "Получение настроек заполнения из 1С")
        val success = loadDefaultWorkOrderSettingsUseCase.loadDefaultWorkOrderSettings() // удаление происходит внутри
        Log.i("rustam", "loadDefaultWorkOrderSettings success = " + success.toString())
    }

    suspend fun refreshWorkOrder() {
        _currentSituation.value = "Получение документов Заказ-наряд из 1С"
        Log.i("rustam", "Получение документов из 1С")
        val success = loadWorkOrdersUseCase.loadWorkOrders()
        Log.i("rustam", "loadWorkOrders success = " + success.toString())
    }

    suspend fun uploadWorkOrders(): FunctionResult {

        _currentSituation.value = "Отправка документов Заказ-наряд в 1С"
        val functionResult = uploadWorkOrdersUseCase.uploadWorkOrders()

        return functionResult
    }

    suspend fun getModifiedWorkOrdersQuantity(): Int {
        // надо через юз кейс делать !!!
        return getModifiedWorkOrdersQuantityUseCase.getModifiedWorkOrdersQuantity()
    }
}
