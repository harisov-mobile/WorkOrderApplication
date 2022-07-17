package ru.internetcloud.workorderapplication.data.repository

import java.lang.Exception
import java.math.BigDecimal
import java.util.Date
import javax.inject.Inject
import ru.internetcloud.workorderapplication.data.datasource.local.CarJobLocalDataSource
import ru.internetcloud.workorderapplication.data.datasource.local.CarLocalDataSource
import ru.internetcloud.workorderapplication.data.datasource.local.CarModelLocalDataSource
import ru.internetcloud.workorderapplication.data.datasource.local.DefaultWorkOrderSettingsLocalDataSource
import ru.internetcloud.workorderapplication.data.datasource.local.DepartmentLocalDataSource
import ru.internetcloud.workorderapplication.data.datasource.local.EmployeeLocalDataSource
import ru.internetcloud.workorderapplication.data.datasource.local.PartnerLocalDataSource
import ru.internetcloud.workorderapplication.data.datasource.local.RepairTypeLocalDataSource
import ru.internetcloud.workorderapplication.data.datasource.local.WorkOrderLocalDataSource
import ru.internetcloud.workorderapplication.data.datasource.local.WorkingHourLocalDataSource
import ru.internetcloud.workorderapplication.data.datasource.remote.AuthRemoteDataSource
import ru.internetcloud.workorderapplication.data.datasource.remote.CarJobRemoteDataSource
import ru.internetcloud.workorderapplication.data.datasource.remote.CarModelRemoteDataSource
import ru.internetcloud.workorderapplication.data.datasource.remote.CarRemoteDataSource
import ru.internetcloud.workorderapplication.data.datasource.remote.DefaultWorkOrderSettingsRemoteDataSource
import ru.internetcloud.workorderapplication.data.datasource.remote.DepartmentRemoteDataSource
import ru.internetcloud.workorderapplication.data.datasource.remote.EmployeeRemoteDataSource
import ru.internetcloud.workorderapplication.data.datasource.remote.PartnerRemoteDataSource
import ru.internetcloud.workorderapplication.data.datasource.remote.RepairTypeRemoteDataSource
import ru.internetcloud.workorderapplication.data.datasource.remote.WorkOrderRemoteDataSource
import ru.internetcloud.workorderapplication.data.datasource.remote.WorkingHourRemoteDataSource
import ru.internetcloud.workorderapplication.data.mapper.DefaultRepairTypeJobDetailMapper
import ru.internetcloud.workorderapplication.data.mapper.JobDetailMapper
import ru.internetcloud.workorderapplication.data.mapper.PerformerDetailMapper
import ru.internetcloud.workorderapplication.data.mapper.RepairTypeMapper
import ru.internetcloud.workorderapplication.data.mapper.WorkOrderMapper
import ru.internetcloud.workorderapplication.data.model.RefreshingResult
import ru.internetcloud.workorderapplication.data.model.WorkOrderWithDetails
import ru.internetcloud.workorderapplication.data.network.api.ApiClient
import ru.internetcloud.workorderapplication.domain.catalog.Car
import ru.internetcloud.workorderapplication.domain.catalog.CarJob
import ru.internetcloud.workorderapplication.domain.catalog.Department
import ru.internetcloud.workorderapplication.domain.catalog.Employee
import ru.internetcloud.workorderapplication.domain.catalog.Partner
import ru.internetcloud.workorderapplication.domain.catalog.RepairType
import ru.internetcloud.workorderapplication.domain.catalog.WorkingHour
import ru.internetcloud.workorderapplication.domain.common.FunctionResult
import ru.internetcloud.workorderapplication.domain.common.SendRequest
import ru.internetcloud.workorderapplication.domain.common.UpdateState
import ru.internetcloud.workorderapplication.domain.document.JobDetail
import ru.internetcloud.workorderapplication.domain.document.PerformerDetail
import ru.internetcloud.workorderapplication.domain.document.WorkOrder
import ru.internetcloud.workorderapplication.domain.repository.SynchroRepository

class SynchroRepositoryImpl @Inject constructor(
    private val jobDetailMapper: JobDetailMapper,
    private val performerDetailMapper: PerformerDetailMapper,
    private val workOrderMapper: WorkOrderMapper,
    private val defaultRepairTypeJobDetailMapper: DefaultRepairTypeJobDetailMapper,
    private val repairTypeMapper: RepairTypeMapper,

    private val authRemoteDataSource: AuthRemoteDataSource,

    private val workOrderLocalDataSource: WorkOrderLocalDataSource,
    private val workOrderRemoteDataSource: WorkOrderRemoteDataSource,

    private val departmentLocalDataSource: DepartmentLocalDataSource,
    private val departmentRemoteDataSource: DepartmentRemoteDataSource,

    private val partnerLocalDataSource: PartnerLocalDataSource,
    private val partnerRemoteDataSource: PartnerRemoteDataSource,

    private val employeeLocalDataSource: EmployeeLocalDataSource,
    private val employeeRemoteDataSource: EmployeeRemoteDataSource,

    private val carJobLocalDataSource: CarJobLocalDataSource,
    private val carJobRemoteDataSource: CarJobRemoteDataSource,

    private val carModelLocalDataSource: CarModelLocalDataSource,
    private val carModelRemoteDataSource: CarModelRemoteDataSource,

    private val carLocalDataSource: CarLocalDataSource,
    private val carRemoteDataSource: CarRemoteDataSource,

    private val workingHourLocalDataSource: WorkingHourLocalDataSource,
    private val workingHourRemoteDataSource: WorkingHourRemoteDataSource,

    private val defWorkOrderSettingsLocal: DefaultWorkOrderSettingsLocalDataSource,
    private val defWorkOrderSettingsRemote: DefaultWorkOrderSettingsRemoteDataSource,

    private val repairTypeLocalDataSource: RepairTypeLocalDataSource,
    private val repairTypeRemoteDataSource: RepairTypeRemoteDataSource

) : SynchroRepository {

    override suspend fun sendWorkOrderToEmail(id: String, email: String): FunctionResult {

        val result = FunctionResult()

        val sendRequest = SendRequest(id = id, email = email)

        try {
            val uploadResponse = ApiClient.getInstance().client.sendWorkOrderToEmail(sendRequest)
            if (uploadResponse.uploadResult.isSuccess) {
                result.isSuccess = true
            } else {
                result.errorMessage = uploadResponse.uploadResult.errorMessage
            }
        } catch (e: Exception) {
            // ничего не делаю
            result.errorMessage = e.toString()
        }

        return result
    }

    override suspend fun uploadWorkOrderById(id: String): FunctionResult {
        val result = FunctionResult()

        val modifiedWorkOrder: WorkOrderWithDetails? = getModifiedWorkOrderById(id)
        if (modifiedWorkOrder == null) {
            // не надо отправлять заказ-наряд на сервер 1С. так как в него не вносились изменения
            result.isSuccess = true
        } else {
            result.amountOfModifiedWorkOrders = 1

            val listWO: MutableList<WorkOrderWithDetails> = mutableListOf()
            listWO.add(modifiedWorkOrder)

            try {
                val uploadWorkOrderResponse = ApiClient.getInstance().client.uploadWorkOrders(listWO)
                if (uploadWorkOrderResponse.uploadResult.isSuccess) {
                    result.isSuccess = true
                } else {
                    result.errorMessage = uploadWorkOrderResponse.uploadResult.errorMessage
                }
            } catch (e: Exception) {
                // ничего не делаю
                result.errorMessage = e.toString()
            }
        }

        return result
    }

    suspend fun getModifiedWorkOrderById(id: String): WorkOrderWithDetails? {
        return workOrderLocalDataSource.getModifiedWorkOrderById(id)
    }

    override suspend fun updateData(): UpdateState {

        var exception: Exception? = null

        val quantity = workOrderLocalDataSource.getModifiedWorkOrdersQuantity()
        if (quantity > 0) {
            // послать новые или измененные заказ-наряды в 1С
            val state = uploadWorkOrdersToServer()
            if (state is UpdateState.ContinueWithoutSynchro || state is UpdateState.Error) {
                return state // послать заказ-наряды в 1С не получилось, выходим
            }
        }

        try {
            val authResponse = authRemoteDataSource.checkAuthorization()
            if (!authResponse.isAuthorized) {
                exception = Exception("You have no authorisation on the 1C server")
            }
        } catch (e: Exception) {
            exception = e
        }

        if (exception != null) {
            // не удалось получить данные из сервера 1С, надо проверить, есть ли данные в БД
            val dbPartnerList = partnerLocalDataSource.getPartnerList()

            if (dbPartnerList.isEmpty()) {
                return UpdateState.Error(exception)
            } else {
                return UpdateState.ContinueWithoutSynchro(exception)
            }
        } else {

            var refreshingResult = refreshDepartment()
            if (refreshingResult is RefreshingResult.Error) {
                return UpdateState.Error(refreshingResult.exception)
            }

            refreshingResult = refreshPartner()
            if (refreshingResult is RefreshingResult.Error) {
                return UpdateState.Error(refreshingResult.exception)
            }

            refreshingResult = refreshEmployee()
            if (refreshingResult is RefreshingResult.Error) {
                return UpdateState.Error(refreshingResult.exception)
            }

            refreshingResult = refreshCarJob()
            if (refreshingResult is RefreshingResult.Error) {
                return UpdateState.Error(refreshingResult.exception)
            }

            refreshingResult = refreshCarModel()
            if (refreshingResult is RefreshingResult.Error) {
                return UpdateState.Error(refreshingResult.exception)
            }

            refreshingResult = refreshCar()
            if (refreshingResult is RefreshingResult.Error) {
                return UpdateState.Error(refreshingResult.exception)
            }

            refreshingResult = refreshWorkingHour()
            if (refreshingResult is RefreshingResult.Error) {
                return UpdateState.Error(refreshingResult.exception)
            }

            refreshingResult = refreshDefaultWorkOrderSettings()
            if (refreshingResult is RefreshingResult.Error) {
                return UpdateState.Error(refreshingResult.exception)
            }

            refreshingResult = refreshRepairType()
            if (refreshingResult is RefreshingResult.Error) {
                return UpdateState.Error(refreshingResult.exception)
            }

            refreshingResult = refreshWorkOrder()
            if (refreshingResult is RefreshingResult.Error) {
                return UpdateState.Error(refreshingResult.exception)
            }
        }

        return UpdateState.Success(quantity)
    }

    suspend fun uploadWorkOrdersToServer(): UpdateState {

        val listWO = workOrderLocalDataSource.getModifiedWorkOrders()

        val result = if (listWO.isEmpty()) {
            UpdateState.Success(listWO.size)
        } else {
            try {
                val uploadWorkOrderResponse = workOrderRemoteDataSource.uploadWorkOrders(listWO)
                if (uploadWorkOrderResponse.uploadResult.isSuccess) {
                    UpdateState.Success(listWO.size)
                } else {
                    UpdateState.ContinueWithoutSynchro(Exception(uploadWorkOrderResponse.uploadResult.errorMessage))
                }
            } catch (e: Exception) {
                UpdateState.ContinueWithoutSynchro(e)
            }
        }

        return result
    }

    suspend fun refreshDepartment(): RefreshingResult {

        departmentLocalDataSource.deleteAllDepartments()

        try {
            val remoteDepartmentList = departmentRemoteDataSource.getDepartmentList()
            if (!remoteDepartmentList.isEmpty()) {
                remoteDepartmentList.forEach {
                    departmentLocalDataSource.addDepartment(it)
                }
            }
        } catch (e: Exception) {
            return RefreshingResult.Error(e)
        }
        return RefreshingResult.Success
    }

    suspend fun refreshPartner(): RefreshingResult {

        partnerLocalDataSource.deleteAllPartners()

        try {
            val remotePartnerList = partnerRemoteDataSource.getPartnerList()
            if (!remotePartnerList.isEmpty()) {
                partnerLocalDataSource.addPartnerList(remotePartnerList)
            }
        } catch (e: Exception) {
            return RefreshingResult.Error(e)
        }
        return RefreshingResult.Success
    }

    suspend fun refreshEmployee(): RefreshingResult {

        employeeLocalDataSource.deleteAllEmployees()

        try {
            val remoteEmployeeList = employeeRemoteDataSource.getEmployeeList()
            if (!remoteEmployeeList.isEmpty()) {
                employeeLocalDataSource.addEmployeeList(remoteEmployeeList)
            }
        } catch (e: Exception) {
            return RefreshingResult.Error(e)
        }
        return RefreshingResult.Success
    }

    suspend fun refreshCarJob(): RefreshingResult {

        carJobLocalDataSource.deleteAllCarJobs()

        try {
            val remoteCarJobList = carJobRemoteDataSource.getCarJobList()
            if (!remoteCarJobList.isEmpty()) {
                carJobLocalDataSource.addCarJobList(remoteCarJobList)
            }
        } catch (e: Exception) {
            return RefreshingResult.Error(e)
        }
        return RefreshingResult.Success
    }

    suspend fun refreshCarModel(): RefreshingResult {

        carModelLocalDataSource.deleteAllCarModels()

        try {
            val remoteCarModelList = carModelRemoteDataSource.getCarModelList()
            if (!remoteCarModelList.isEmpty()) {
                carModelLocalDataSource.addCarModelList(remoteCarModelList)
            }
        } catch (e: Exception) {
            return RefreshingResult.Error(e)
        }
        return RefreshingResult.Success
    }

    suspend fun refreshCar(): RefreshingResult {

        carLocalDataSource.deleteAllCars()

        try {
            val remoteCarList = carRemoteDataSource.getCarList()
            if (!remoteCarList.isEmpty()) {
                carLocalDataSource.addCarList(remoteCarList)
            }
        } catch (e: Exception) {
            return RefreshingResult.Error(e)
        }
        return RefreshingResult.Success
    }

    suspend fun refreshWorkingHour(): RefreshingResult {

        workingHourLocalDataSource.deleteAllWorkingHours()

        try {
            val remoteWorkingHourList = workingHourRemoteDataSource.getWorkingHourList()
            if (!remoteWorkingHourList.isEmpty()) {
                workingHourLocalDataSource.addWorkingHourList(remoteWorkingHourList)
            }
        } catch (e: Exception) {
            return RefreshingResult.Error(e)
        }
        return RefreshingResult.Success
    }

    suspend fun refreshDefaultWorkOrderSettings(): RefreshingResult {

        defWorkOrderSettingsLocal.deleteAllDefaultWorkOrderSettings()

        try {
            val remoteDefSettingsList = defWorkOrderSettingsRemote.getDefaultWorkOrderSettings()
            if (!remoteDefSettingsList.isEmpty()) {
                defWorkOrderSettingsLocal.addDefaultWorkOrderSettingsList(remoteDefSettingsList)
            }
        } catch (e: Exception) {
            return RefreshingResult.Error(e)
        }
        return RefreshingResult.Success
    }

    suspend fun refreshRepairType(): RefreshingResult {

        repairTypeLocalDataSource.deleteAllRepairTypes()
        repairTypeLocalDataSource.deleteAllDefaultRepairTypeJobDetails()

        try {
            val repairTypeResponse = repairTypeRemoteDataSource.getRepairTypeResponse()

            repairTypeLocalDataSource.addRepairTypeList(
                repairTypeMapper.fromListDtoToListEntity(repairTypeResponse.repairTypes)
            )

            repairTypeLocalDataSource.addDefaultRepairTypeJobDetailList(
                defaultRepairTypeJobDetailMapper.fromListDtoToDbModel(repairTypeResponse.defaultJobDetails)
            )
        } catch (e: Exception) {
            return RefreshingResult.Error(e)
        }

        return RefreshingResult.Success
    }

    suspend fun refreshWorkOrder(): RefreshingResult {

        workOrderLocalDataSource.deleteAllJobDetails()
        workOrderLocalDataSource.deleteAllPerformers()
        workOrderLocalDataSource.deleteAllWorkOrders()

        try {
            val workOrderResponse = workOrderRemoteDataSource.getWorkOrders()

            workOrderLocalDataSource.addJobDetailList(
                jobDetailMapper.fromListDtoToDbModel(workOrderResponse.jobDetails)
            )

            workOrderLocalDataSource.addPerformerDetailList(
                performerDetailMapper.fromListDtoToDbModel(workOrderResponse.performerDetails)
            )

            workOrderLocalDataSource.addWorkOrderList(
                workOrderMapper.fromListDtoToListDboModel(workOrderResponse.workOrders)
            )
        } catch (e: Exception) {
            return RefreshingResult.Error(e)
        }

        return RefreshingResult.Success
    }

    override suspend fun loadMockData() {
        partnerLocalDataSource.deleteAllPartners()
        employeeLocalDataSource.deleteAllEmployees()

        carJobLocalDataSource.deleteAllCarJobs()
        carModelLocalDataSource.deleteAllCarModels()
        carLocalDataSource.deleteAllCars()

        departmentLocalDataSource.deleteAllDepartments()
        workingHourLocalDataSource.deleteAllWorkingHours()
        defWorkOrderSettingsLocal.deleteAllDefaultWorkOrderSettings()

        repairTypeLocalDataSource.deleteAllRepairTypes()
        repairTypeLocalDataSource.deleteAllDefaultRepairTypeJobDetails()

        workOrderLocalDataSource.deleteAllJobDetails()
        workOrderLocalDataSource.deleteAllPerformers()
        workOrderLocalDataSource.deleteAllWorkOrders()

        createMockData()
    }

    suspend fun createMockData() {
        val repairTypeList = mutableListOf<RepairType>()
        val repairType1 = RepairType(id = "1", "Paid repair")
        repairTypeList.add(repairType1)
        repairTypeList.add(RepairType(id = "2", "Maintenance"))
        repairTypeList.add(RepairType(id = "3", "Warranty repair"))
        repairTypeLocalDataSource.addRepairTypeList(repairTypeList)

        val partnerList = mutableListOf<Partner>()

        val partner1 = Partner(id = "1", name = "Company A", fullName = "Company A, Apple", inn = "00001")

        partnerList.add(partner1)
        partnerList.add(Partner(id = "2", name = "Company B", fullName = "Company B, Bridge", inn = "00002"))
        partnerList.add(Partner(id = "3", name = "Company C", fullName = "Company C, Compex", inn = "00003"))
        partnerLocalDataSource.addPartnerList(partnerList)

        val employeeList = mutableListOf<Employee>()
        val employee1 = Employee(id = "1", name = "John Smith")
        val employee2 = Employee(id = "3", name = "Ivan Dragov")
        employeeList.add(employee1)
        employeeList.add(Employee(id = "2", name = "Lily Anderson"))
        employeeList.add(employee2)
        employeeLocalDataSource.addEmployeeList(employeeList)

        val carJobList = mutableListOf<CarJob>()
        val carJob1 = CarJob(id = "1", name = "Gearbox restoration", folder = "Transmission")
        val carJob3 = CarJob(id = "3", name = "Removing the rotor pulley.", folder = "Injection system")
        carJobList.add(carJob1)
        carJobList.add(CarJob(id = "2", name = "Compression measurement", folder = "Engine"))
        carJobList.add(carJob3)
        carJobLocalDataSource.addCarJobList(carJobList)

        val carList = mutableListOf<Car>()
        val car1 = Car(id = "1", name = "CAR1", vin = "vin_001", manufacturer = "CLASS", owner = partner1)
        carList.add(car1)
        carList.add(Car(id = "2", name = "CAR2", vin = "vin_002", manufacturer = "MACDON", owner = partner1))
        carList.add(Car(id = "3", name = "CAR3", vin = "vin_003", manufacturer = "AMAZONE", owner = partner1))
        carLocalDataSource.addCarList(carList)

        val workingHourList = mutableListOf<WorkingHour>()
        val wh1 = WorkingHour(id = "1", name = "WH1", price = BigDecimal("2.5"))
        workingHourList.add(wh1)
        workingHourList.add(WorkingHour(id = "2", name = "WH2", price = BigDecimal("10.99")))
        workingHourList.add(WorkingHour(id = "3", name = "WH3", price = BigDecimal("30")))
        workingHourLocalDataSource.addWorkingHourList(workingHourList)

        val department1 = Department(id = "1", name = "Dep1")
        departmentLocalDataSource.addDepartment(department1)
        departmentLocalDataSource.addDepartment(Department(id = "2", name = "Dep2"))
        departmentLocalDataSource.addDepartment(Department(id = "3", name = "Dep3"))

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
                sum = sum
            )
        )

        workOrderLocalDataSource.updateWorkOrder(
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
