package ru.internetcloud.workorderapplication.presentation.synchro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.internetcloud.workorderapplication.di.qualifiers.usecase.*
import ru.internetcloud.workorderapplication.domain.catalog.Department
import ru.internetcloud.workorderapplication.domain.common.FunctionResult
import ru.internetcloud.workorderapplication.domain.common.OperationMode
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.car.AddCarListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.car.DeleteCarListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.car.GetCarListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.carjob.AddCarJobListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.carjob.DeleteCarJobsUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.carjob.GetCarJobListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.carmodel.AddCarModelListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.carmodel.DeleteCarModelsUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.carmodel.GetCarModelListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.department.AddDepartmentUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.department.DeleteDepartmentsUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.department.GetDepartmentListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.employee.AddEmployeeListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.employee.DeleteEmployeesUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.employee.GetEmployeeListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.partner.AddPartnerListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.partner.DeletePartnerListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.partner.GetPartnerListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.workinghour.AddWorkingHourListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.workinghour.DeleteWorkingHourListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.workinghour.GetWorkingHourListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.synchrooperation.*
import javax.inject.Inject

class DataSynchronizationFragmentViewModel @Inject constructor(

    private val loadRepairTypesUseCase: LoadRepairTypesUseCase,

    @DbGetDepartmentListUseCaseQualifier
    private val getDbDepartmentListUseCase: GetDepartmentListUseCase,

    @RemoteGetCarJobListUseCaseQualifier
    private val getRemoteCarJobListUseCase: GetCarJobListUseCase,

    private val addDbCarJobListUseCase: AddCarJobListUseCase,
    private val deleteCarJobsUseCase: DeleteCarJobsUseCase,

    @RemoteGetCarModelListUseCaseQualifier
    private val getRemoteCarModelListUseCase: GetCarModelListUseCase,

    private val addDbCarModelListUseCase: AddCarModelListUseCase,
    private val deleteCarModelsUseCase: DeleteCarModelsUseCase,

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

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _currentSituation = MutableLiveData<OperationMode>()
    val currentSituation: LiveData<OperationMode>
        get() = _currentSituation

    private val _uploadResult = MutableLiveData<FunctionResult>()
    val uploadResult: LiveData<FunctionResult>
        get() = _uploadResult

    fun synchonizeData() {
        viewModelScope.launch {

            var success = true

            val quantity = getModifiedWorkOrdersQuantity()
            if (quantity > 0) {
                // послать новые или измененные заказ-наряды в 1С
                val result = uploadWorkOrders()
                _uploadResult.value = result
                success = result.isSuccess
                if (!success) {
                    _canContinueWithoutSynchro.value = true // послать заказ-наряды в 1С не получилось
                }
            }

            if (success) {
                var result = true
                var remoteDepartmentList = listOf<Department>()
                try {
                    remoteDepartmentList = getRemoteDepartmentListUseCase.getDepartmentList()
                } catch (e: Exception) {
                    result = false
                    _errorMessage.value = "remoteDepartmentList: " + e.toString()
                }

                if (!result) {
                    // не удалось получить данные из сервера 1С, надо проверить, есть ли данные в БД
                    val dbDepartmentList = getDbDepartmentListUseCase.getDepartmentList()

                    if (dbDepartmentList.isEmpty()) {
                        _errorSynchronization.value = true
                    } else {
                        _canContinueWithoutSynchro.value = true
                    }
                } else {
                    if (refreshDepartment() &&
                        refreshPartner() &&
                        refreshEmployee() &&
                        refreshCarJob() &&
                        refreshCarModel() &&
                        refreshCar() &&
                        refreshWorkingHour() &&
                        refreshDefaultWorkOrderSettings() &&
                        refreshRepairType() &&
                        refreshWorkOrder()) {

                        _currentSituation.value = OperationMode.NOTHING
                        _canContinue.value = true
                    } else {
                        _errorSynchronization.value = true
                    }
                }
            }
        }
    }

    suspend fun refreshCarJob(): Boolean {

        var result = true

        deleteCarJobsUseCase.deleteAllCarJobs()

        try {
            _currentSituation.value = OperationMode.GET_CAR_JOB_LIST // "Получение справочника Автоработы из 1С"
            val remoteCarJobList = getRemoteCarJobListUseCase.getCarJobList()
            if (!remoteCarJobList.isEmpty()) {
                _currentSituation.value = OperationMode.PREPARE_CAR_JOB_LIST // "Обработка справочника Автоработы"
                addDbCarJobListUseCase.addCarJobList(remoteCarJobList)
            }
        } catch (e: Exception) {
            result = false
            _errorMessage.value = "refreshCarJob: " + e.toString()
        }
        return result
    }

    suspend fun refreshCarModel(): Boolean {

        var result = true

        deleteCarModelsUseCase.deleteAllCarModels()

        try {
            _currentSituation.value = OperationMode.GET_CAR_MODEL_LIST // "Получение справочника Модели из 1С"
            val remoteCarModelList = getRemoteCarModelListUseCase.getCarModelList()
            if (!remoteCarModelList.isEmpty()) {
                _currentSituation.value = OperationMode.PREPARE_CAR_MODEL_LIST // "Обработка справочника Модели"
                addDbCarModelListUseCase.addCarModelList(remoteCarModelList)
            }
        } catch (e: Exception) {
            result = false
            _errorMessage.value = "refreshCarModel: " + e.toString()
        }
        return result
    }

    suspend fun refreshDepartment(): Boolean {

        var result = true

        _currentSituation.value = OperationMode.GET_DEPARTMENT_LIST // "Получение справочника Цеха из 1С"

        deleteDepartmentsUseCase.deleteAllDepartments()

        try {
            val remoteDepartmentList = getRemoteDepartmentListUseCase.getDepartmentList()
            if (!remoteDepartmentList.isEmpty()) {
                _currentSituation.value = OperationMode.PREPARE_DEPARTMENT_LIST // "Обработка справочника Цеха"
                remoteDepartmentList.forEach {
                    addDbDepartmentUseCase.addDepartment(it)
                }
            }
        } catch (e: Exception) {
            result = false
            _errorMessage.value = "refreshDepartment: " + e.toString()
        }
        return result
    }

    suspend fun refreshEmployee(): Boolean {

        var result = true

        _currentSituation.value = OperationMode.GET_EMPLOYEE_LIST // "Получение справочника Сотрудники из 1С"

        deleteEmployeesUseCase.deleteAllEmployees()

        try {
            val remoteEmployeeList = getRemoteEmployeeListUseCase.getEmployeeList()
            if (!remoteEmployeeList.isEmpty()) {
                _currentSituation.value = OperationMode.PREPARE_EMPLOYEE_LIST // "Обработка справочника Сотрудники"
                addDbEmployeeListUseCase.addEmployeeList(remoteEmployeeList)
            }
        } catch (e: Exception) {
            result = false
            _errorMessage.value = "refreshEmployee: " + e.toString()
        }
        return result
    }

    suspend fun refreshPartner(): Boolean {

        var result = true

        _currentSituation.value = OperationMode.GET_PARTNER_LIST // "Получение справочника Контрагенты из 1С"

        deletePartnersUseCase.deleteAllPartners()

        try {
            val remotePartnerList = getRemotePartnerListUseCase.getPartnerList()
            if (!remotePartnerList.isEmpty()) {
                _currentSituation.value = OperationMode.PREPARE_PARTNER_LIST // "Обработка справочника Контрагенты"
                addDbPartnerListUseCase.addPartnerList(remotePartnerList)
            }
        } catch (e: Exception) {
            result = false
            _errorMessage.value = "refreshPartner: " + e.toString()
        }
        return result
    }

    suspend fun refreshCar(): Boolean {

        var result = true

        _currentSituation.value = OperationMode.GET_CAR_LIST // "Получение справочника СХТ из 1С"

        deleteCarsUseCase.deleteAllCars()

        try {
            val remoteCarList = getRemoteCarListUseCase.getCarList()
            if (!remoteCarList.isEmpty()) {
                _currentSituation.value = OperationMode.PREPARE_CAR_LIST // "Обработка справочника СХТ"
                addDbCarListUseCase.addCarList(remoteCarList)
            }
        } catch (e: Exception) {
            result = false
            _errorMessage.value = "refreshCar: " + e.toString()
        }
        return result
    }

    suspend fun refreshWorkingHour(): Boolean {

        var result = true

        _currentSituation.value = OperationMode.GET_WORKING_HOUR_LIST // "Получение справочника Нормочасы из 1С"

        deleteWorkingHoursUseCase.deleteAllWorkingHours()

        try {
            val remoteWorkingHourList = getRemoteWorkingHourListUseCase.getWorkingHourList()
            if (!remoteWorkingHourList.isEmpty()) {
                _currentSituation.value = OperationMode.PREPARE_WORKING_HOUR_LIST // "Обработка справочника Нормочасы"
                addDbWorkingHourListUseCase.addWorkingHourList(remoteWorkingHourList)
            }
        } catch (e: Exception) {
            result = false
            _errorMessage.value = "refreshWorkingHour: " + e.toString()
        }
        return result
    }

    suspend fun refreshDefaultWorkOrderSettings(): Boolean {

        var result = true

        _currentSituation.value = OperationMode.GET_DEFAULT_WORK_ORDER_SETTINGS // "Получение настроек заполнения из 1С"

        try {
            loadDefaultWorkOrderSettingsUseCase.loadDefaultWorkOrderSettings() // удаление происходит внутри
        } catch (e: Exception) {
            result = false
            _errorMessage.value = "refreshDefaultWorkOrderSettings: " + e.toString()
        }
        return result
    }

    suspend fun refreshWorkOrder(): Boolean {

        var result = true

        _currentSituation.value = OperationMode.LOAD_WORK_ORDERS // "Получение документов Заказ-наряд из 1С"

        try {
            loadWorkOrdersUseCase.loadWorkOrders()
        } catch (e: Exception) {
            result = false
            _errorMessage.value = "refreshPartner: " + e.toString()
        }
        return result
    }

    suspend fun refreshRepairType(): Boolean {

        var result = true

        _currentSituation.value = OperationMode.LOAD_REPAIR_TYPES

        try {
            loadRepairTypesUseCase.loadRepairTypes()
        } catch (e: Exception) {
            result = false
            _errorMessage.value = "refreshRepairType: " + e.toString()
        }
        return result
    }

    suspend fun uploadWorkOrders(): FunctionResult {

        _currentSituation.value = OperationMode.UPLOAD_WORK_ORDERS //  "Отправка документов Заказ-наряд в 1С"
        val functionResult = uploadWorkOrdersUseCase.uploadWorkOrders()

        return functionResult
    }

    suspend fun getModifiedWorkOrdersQuantity(): Int {
        return getModifiedWorkOrdersQuantityUseCase.getModifiedWorkOrdersQuantity()
    }
}
