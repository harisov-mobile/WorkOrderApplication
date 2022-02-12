package ru.internetcloud.workorderapplication.presentation.logon

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.internetcloud.workorderapplication.data.repository.AuthRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.SynchroRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.db.*
import ru.internetcloud.workorderapplication.domain.catalog.*
import ru.internetcloud.workorderapplication.domain.document.JobDetail
import ru.internetcloud.workorderapplication.domain.document.PerformerDetail
import ru.internetcloud.workorderapplication.domain.document.WorkOrder
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.car.AddCarListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.car.DeleteCarListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.carjob.AddCarJobListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.carjob.DeleteCarJobsUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.department.AddDepartmentUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.department.DeleteDepartmentsUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.employee.AddEmployeeListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.employee.DeleteEmployeesUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.partner.AddPartnerListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.partner.DeletePartnerListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.repairtype.AddRepairTypeListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.repairtype.DeleteRepairTypesUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.workinghour.AddWorkingHourListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.workinghour.DeleteWorkingHourListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.documentoperation.*
import ru.internetcloud.workorderapplication.domain.usecase.logonoperation.CheckAuthParametersUseCase
import ru.internetcloud.workorderapplication.domain.usecase.logonoperation.SetAuthParametersUseCase
import ru.internetcloud.workorderapplication.domain.usecase.settingsoperation.DeleteAllDefaultWorkOrderSettingsUseCase
import java.math.BigDecimal
import java.util.*

class LogonViewModel(private val app: Application) : AndroidViewModel(app) {

    companion object {
        private const val BEGIN_SIZE = 4
        private const val HTTP_PREFIX = "https://"
        private const val DEMO_SERVER = "demo"
        private const val DEMO_LOGIN = "demo"
        private const val DEMO_PASSWORD = "1"
    }

    // Репозитории
    private val repository = AuthRepositoryImpl.get() // требуется инъекция зависимостей!!!
    private val dbRepairTypeRepository = DbRepairTypeRepositoryImpl.get() // требуется инъекция зависимостей!!!
    private val dbCarJobRepository = DbCarJobRepositoryImpl.get() // требуется инъекция зависимостей!!!
    private val dbDepartmentRepository = DbDepartmentRepositoryImpl.get() // требуется инъекция зависимостей!!!
    private val dbEmployeeRepository = DbEmployeeRepositoryImpl.get() // требуется инъекция зависимостей!!!
    private val dbPartnerRepository = DbPartnerRepositoryImpl.get() // требуется инъекция зависимостей!!!
    private val dbCarRepository = DbCarRepositoryImpl.get() // требуется инъекция зависимостей!!!
    private val dbWorkingHourRepository = DbWorkingHourRepositoryImpl.get() // требуется инъекция зависимостей!!!
    private val dbDefaultWorkOrderSettingsRepository =
        DbDefaultWorkOrderSettingsRepositoryImpl.get() // требуется инъекция зависимостей!!!
    private val synchroRepository = SynchroRepositoryImpl.get() // требуется инъекция зависимостей!!!
    private val dbWorkOrderRepository = DbWorkOrderRepositoryImpl.get() // требуется инъекция зависимостей!!!

    // ссылки на экземпляры классов Юзе-Кейсов, которые будут использоваться в Вью-Модели:
    private val setAuthParametersUseCase = SetAuthParametersUseCase(repository)
    private val checkAuthParametersUseCase = CheckAuthParametersUseCase(repository)
    private val deleteRepairTypesUseCase = DeleteRepairTypesUseCase(dbRepairTypeRepository)
    private val deletePartnersUseCase = DeletePartnerListUseCase(dbPartnerRepository)
    private val deleteCarJobsUseCase = DeleteCarJobsUseCase(dbCarJobRepository)
    private val deleteDepartmentsUseCase = DeleteDepartmentsUseCase(dbDepartmentRepository)
    private val deleteEmployeesUseCase = DeleteEmployeesUseCase(dbEmployeeRepository)
    private val deleteCarsUseCase = DeleteCarListUseCase(dbCarRepository)
    private val deleteWorkingHoursUseCase = DeleteWorkingHourListUseCase(dbWorkingHourRepository)
    private val deleteAllDefaultWorkOrderSettingsUseCase =
        DeleteAllDefaultWorkOrderSettingsUseCase(dbDefaultWorkOrderSettingsRepository)
    private val deleteAllJobDetailsUseCase = DeleteAllJobDetailsUseCase(synchroRepository)
    private val deleteAllPerformersUseCase = DeleteAllPerformersUseCase(synchroRepository)
    private val deleteAllWorkOrdersUseCase = DeleteAllWorkOrdersUseCase(synchroRepository)

    private val addRepairTypeListUseCase = AddRepairTypeListUseCase(dbRepairTypeRepository)
    private val addPartnerListUseCase = AddPartnerListUseCase(dbPartnerRepository)
    private val addEmployeeListUseCase = AddEmployeeListUseCase(dbEmployeeRepository)
    private val addCarJobListUseCase = AddCarJobListUseCase(dbCarJobRepository)
    private val addCarListUseCase = AddCarListUseCase(dbCarRepository)
    private val addDepartmentUseCase = AddDepartmentUseCase(dbDepartmentRepository)
    private val addWorkingHourListUseCase = AddWorkingHourListUseCase(dbWorkingHourRepository)
    private val updateWorkOrderUseCase = UpdateWorkOrderUseCase(dbWorkOrderRepository)

    private val _canContinue = MutableLiveData<Boolean>()
    val canContinue: LiveData<Boolean>
        get() = _canContinue

    private val _demoMode = MutableLiveData<Boolean>()
    val demoMode: LiveData<Boolean>
        get() = _demoMode

    private val _canContinueDemoMode = MutableLiveData<Boolean>()
    val canContinueDemoMode: LiveData<Boolean>
        get() = _canContinueDemoMode

    private val _errorInputServer = MutableLiveData<Boolean>()
    val errorInputServer: LiveData<Boolean>
        get() = _errorInputServer

    private val _errorInputLogin = MutableLiveData<Boolean>()
    val errorInputLogin: LiveData<Boolean>
        get() = _errorInputLogin

    private val _errorInputPassword = MutableLiveData<Boolean>()
    val errorInputPassword: LiveData<Boolean>
        get() = _errorInputPassword

    private val _errorAuthorization = MutableLiveData<Boolean>()
    val errorAuthorization: LiveData<Boolean>
        get() = _errorAuthorization

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun signin(inputServer: String?, inputLogin: String?, inputPassword: String?) {
        var server = parseText(inputServer)
        val login = parseText(inputLogin)
        val password = parseText(inputPassword)

        val areFieldsValid = validateInput(server, login, password)

        if (areFieldsValid) {
            viewModelScope.launch {

                server = server.lowercase()

                // демо-режим:
                if (server.equals(DEMO_SERVER) && login.equals(DEMO_LOGIN) && password.equals(DEMO_PASSWORD)) {
                    setAuthParametersUseCase.setAuthParameters(server, login, password)
                    _demoMode.value = true
                } else {
                    if (server.length >= BEGIN_SIZE) {
                        val firstFourLetters = server.substring(0, BEGIN_SIZE)
                        if (!firstFourLetters.equals("http")) {
                            server = HTTP_PREFIX + server
                        }
                    } else {
                        server = HTTP_PREFIX + server
                    }
                    Log.i("rustam", "Имя сервера = " + server)
                    Log.i("rustam", "Логин = " + login)
                    Log.i("rustam", "Пароль = " + password)

                    setAuthParametersUseCase.setAuthParameters(server, login, password)

                    // сделать проверку пароля!!!!
                    val authResult = checkAuthParametersUseCase.checkAuthorization()
                    if (authResult.isAuthorized) {
                        _canContinue.value = true
                    } else {
                        _errorMessage.value = authResult.errorMessage
                        _errorAuthorization.value = true
                    }
                }

            }
        }
    }

    private fun parseText(input: String?): String {
        return input?.trim() ?: ""
    }

    private fun validateInput(server: String, login: String, password: String): Boolean {
        var result = true
        if (server.isBlank()) {
            _errorInputServer.value = true
            result = false
        }

        if (login.isBlank()) {
            _errorInputLogin.value = true
            result = false
        }

        if (password.isBlank()) {
            _errorInputPassword.value = true
            result = false
        }

        return result
    }

    fun resetErrorInputServer() {
        _errorInputServer.value = false
    }

    fun resetErrorInputLogin() {
        _errorInputLogin.value = false
    }

    fun resetErrorInputPassword() {
        _errorInputPassword.value = false
    }

    fun resetCanContinue() {
        _canContinue.value = false
    }

    fun loadDemoData() {
        viewModelScope.launch {
            deleteAllData()
            loadMockData()

            _canContinueDemoMode.value = true
        }
    }

    suspend fun deleteAllData() {
        deleteRepairTypesUseCase.deleteAllRepairTypes()
        deletePartnersUseCase.deleteAllPartners()
        deleteEmployeesUseCase.deleteAllEmployees()
        deleteCarJobsUseCase.deleteAllCarJobs()
        deleteCarsUseCase.deleteAllCars()
        deleteDepartmentsUseCase.deleteAllDepartments()
        deleteWorkingHoursUseCase.deleteAllWorkingHours()
        deleteAllDefaultWorkOrderSettingsUseCase.deleteAllDefaultWorkOrderSettings()
        deleteAllJobDetailsUseCase.deleteAllJobDetails()
        deleteAllPerformersUseCase.deleteAllPerformers()
        deleteAllWorkOrdersUseCase.deleteAllWorkOrders()
    }

    suspend fun loadMockData() {
        val repairTypeList = mutableListOf<RepairType>()
        val repairType1 = RepairType(id = "1", "Paid repair")
        repairTypeList.add(repairType1)
        repairTypeList.add(RepairType(id = "2", "Maintenance"))
        repairTypeList.add(RepairType(id = "3", "Warranty repair"))
        addRepairTypeListUseCase.addRepairTypeList(repairTypeList)

        val partnerList = mutableListOf<Partner>()

        val partner1 = Partner(id = "1", name = "Company A", fullName = "Company A, Apple", inn = "00001")

        partnerList.add(partner1)
        partnerList.add(Partner(id = "2", name = "Company B", fullName = "Company B, Bridge", inn = "00002"))
        partnerList.add(Partner(id = "3", name = "Company C", fullName = "Company C, Compex", inn = "00003"))
        addPartnerListUseCase.addPartnerList(partnerList)

        val employeeList = mutableListOf<Employee>()
        val employee1 = Employee(id = "1", name = "John Smith")
        val employee2 = Employee(id = "3", name = "Ivan Dragov")
        employeeList.add(employee1)
        employeeList.add(Employee(id = "2", name = "Lily Anderson"))
        employeeList.add(employee2)
        addEmployeeListUseCase.addEmployeeList(employeeList)

        val carJobList = mutableListOf<CarJob>()
        val carJob1 = CarJob(id = "1", name = "Gearbox restoration", folder = "Transmission")
        val carJob3 = CarJob(id = "3", name = "Removing the rotor pulley.", folder = "Injection system")
        carJobList.add(carJob1)
        carJobList.add(CarJob(id = "2", name = "Compression measurement", folder = "Engine"))
        carJobList.add(carJob3)
        addCarJobListUseCase.addCarJobList(carJobList)

        val carList = mutableListOf<Car>()
        val car1 = Car(id = "1", name = "CAR1", vin = "vin_001", manufacturer = "CLASS")
        carList.add(car1)
        carList.add(Car(id = "2", name = "CAR2", vin = "vin_002", manufacturer = "MACDON"))
        carList.add(Car(id = "3", name = "CAR3", vin = "vin_003", manufacturer = "AMAZONE"))
        addCarListUseCase.addCarList(carList)

        val workingHourList = mutableListOf<WorkingHour>()
        val wh1 = WorkingHour(id = "1", name = "WH1", price = BigDecimal("2.5"))
        workingHourList.add(wh1)
        workingHourList.add(WorkingHour(id = "2", name = "WH2", price = BigDecimal("10.99")))
        workingHourList.add(WorkingHour(id = "3", name = "WH3", price = BigDecimal("30")))
        addWorkingHourListUseCase.addWorkingHourList(workingHourList)

        val department1 = Department(id = "1", name = "Dep1")
        addDepartmentUseCase.addDepartment(department1)
        addDepartmentUseCase.addDepartment(Department(id = "2", name = "Dep2"))
        addDepartmentUseCase.addDepartment(Department(id = "3", name = "Dep3"))

        val performers = mutableListOf<PerformerDetail>()
        performers.add(PerformerDetail(id = "1", lineNumber = 1, employee = employee2))

        val quantity: BigDecimal = BigDecimal("1")
        val timeNorm: BigDecimal = BigDecimal("2")
        val sum: BigDecimal = quantity * timeNorm * wh1.price

        val jobDetails = mutableListOf<JobDetail>()
        jobDetails.add(
            JobDetail(
                id = "1",
                lineNumber = 1,
                carJob = carJob1,
                quantity = quantity,
                timeNorm = timeNorm,
                workingHour = wh1,
                sum = sum,
            )
        )

        updateWorkOrderUseCase.updateWorkOrder(
            WorkOrder(
                id = "1",
                number = "WO_0001",
                date = Date(),
                partner = partner1,
                car = car1,
                repairType = repairType1,
                department = department1,
                master = employee1,
                mileage = 4500,
                performers = performers,
                performersString = employee2.name,
                jobDetails = jobDetails
            )
        )
    }
}
