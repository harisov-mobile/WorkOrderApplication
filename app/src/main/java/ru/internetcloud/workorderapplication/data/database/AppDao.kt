package ru.internetcloud.workorderapplication.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.sqlite.db.SupportSQLiteQuery
import ru.internetcloud.workorderapplication.data.model.CarDbModel
import ru.internetcloud.workorderapplication.data.model.CarJobDbModel
import ru.internetcloud.workorderapplication.data.model.CarModelDbModel
import ru.internetcloud.workorderapplication.data.model.CarWithOwner
import ru.internetcloud.workorderapplication.data.model.DefaultRepairTypeJobDetailDbModel
import ru.internetcloud.workorderapplication.data.model.DefaultRepairTypeJobDetailWithRequisities
import ru.internetcloud.workorderapplication.data.model.DefaultWorkOrderSettingsDbModel
import ru.internetcloud.workorderapplication.data.model.DefaultWorkOrderSettingsWithRequisities
import ru.internetcloud.workorderapplication.data.model.DepartmentDbModel
import ru.internetcloud.workorderapplication.data.model.EmployeeDbModel
import ru.internetcloud.workorderapplication.data.model.JobDetailDbModel
import ru.internetcloud.workorderapplication.data.model.PartnerDbModel
import ru.internetcloud.workorderapplication.data.model.PerformerDetailDbModel
import ru.internetcloud.workorderapplication.data.model.RepairTypeDbModel
import ru.internetcloud.workorderapplication.data.model.WorkOrderDbModel
import ru.internetcloud.workorderapplication.data.model.WorkOrderWithDetails
import ru.internetcloud.workorderapplication.data.model.WorkingHourDbModel

@Dao
interface AppDao {

    @Transaction
    @Query("SELECT * FROM work_orders ORDER BY date, number")
    fun getWorkOrderList(): LiveData<List<WorkOrderWithDetails>> // Не использовать LiveData в репозитории

    @Transaction
    @RawQuery
    fun getFilteredWorkOrderList(query: SupportSQLiteQuery): LiveData<List<WorkOrderWithDetails>> // Не использовать LiveData в репозитории

    @Transaction
    @Query("SELECT * FROM work_orders WHERE isModified")
    suspend fun getModifiedWorkOrders(): List<WorkOrderWithDetails>

    @Transaction
    @Query("SELECT * FROM work_orders WHERE isModified AND id=:id")
    suspend fun getModifiedWorkOrderById(id: String): WorkOrderWithDetails?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addWorkOrder(workOrderDbModel: WorkOrderDbModel)

    @Query("SELECT * FROM work_orders WHERE id=:workOrderId LIMIT 1")
    suspend fun getWorkOrder(workOrderId: String): WorkOrderWithDetails? // Андрей Сумин почему-то не Лив дату возвращает...

    @Query("DELETE FROM work_orders")
    suspend fun deleteAllWorkOrders()

    @Query("DELETE FROM job_details")
    suspend fun deleteAllJobDetails()

    @Query("DELETE FROM default_repair_type_job_details")
    suspend fun deleteAllDefaultRepairTypeJobDetails()

    @Query("DELETE FROM default_work_order_settings")
    suspend fun deleteAllDefaultWorkOrderSettings()

    @Query("DELETE FROM performer_details")
    suspend fun deleteAllPerformers()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addJobDetailList(jobDetailDbModelList: List<JobDetailDbModel>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addDefaultRepairTypeJobDetailList(defaultJobDetailDbModelList: List<DefaultRepairTypeJobDetailDbModel>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPerformerDetailList(performerDetailDbModelList: List<PerformerDetailDbModel>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addWorkOrderList(workOrderDbModelList: List<WorkOrderDbModel>)
    // ----------------------------------------------------------------------
    @Query("SELECT * FROM repair_types")
    suspend fun getRepairTypeList(): List<RepairTypeDbModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRepairType(repairTypeDbModel: RepairTypeDbModel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addRepairTypeList(repairTypeDbModelList: List<RepairTypeDbModel>)

    @Query("DELETE FROM repair_types")
    suspend fun deleteAllRepairTypes()

    @Query("SELECT * FROM repair_types WHERE id=:id LIMIT 1")
    suspend fun getRepairType(id: String): RepairTypeDbModel?

    @Query("SELECT * FROM repair_types WHERE name LIKE :searchText")
    suspend fun searhRepairTypes(searchText: String): List<RepairTypeDbModel>

    @Query("SELECT * FROM default_repair_type_job_details WHERE repairTypeId=:id")
    suspend fun getDefaultRepairTypeJobDetails(id: String): List<DefaultRepairTypeJobDetailWithRequisities>
    // ----------------------------------------------------------------------

    @Query("SELECT * FROM car_jobs")
    suspend fun getCarJobList(): List<CarJobDbModel>

    @Query("SELECT * FROM car_models")
    suspend fun getCarModelList(): List<CarModelDbModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCarJob(carJobDbModel: CarJobDbModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCarModel(carModelDbModel: CarModelDbModel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCarJobList(carJobDbModelList: List<CarJobDbModel>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCarModelList(carModelDbModelList: List<CarModelDbModel>)

    @Query("DELETE FROM car_jobs")
    suspend fun deleteAllCarJobs()

    @Query("DELETE FROM car_models")
    suspend fun deleteAllCarModels()

    @Query("SELECT * FROM car_jobs WHERE id=:id LIMIT 1")
    suspend fun getCarJob(id: String): CarJobDbModel?

    @Query("SELECT * FROM car_models WHERE id=:id LIMIT 1")
    suspend fun getCarModel(id: String): CarModelDbModel?

    @Query("SELECT * FROM car_jobs WHERE name LIKE :searchText")
    suspend fun searhCarJobs(searchText: String): List<CarJobDbModel>

    // ----------------------------------------------------------------------
    @Query("SELECT * FROM departments")
    suspend fun getDepartmentList(): List<DepartmentDbModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDepartment(departmentDbModel: DepartmentDbModel)

    @Query("DELETE FROM departments")
    suspend fun deleteAllDepartments()

    @Query("SELECT * FROM departments WHERE id=:id LIMIT 1")
    suspend fun getDepartment(id: String): DepartmentDbModel?

    @Query("SELECT * FROM departments WHERE name LIKE :searchText")
    suspend fun searhDepartments(searchText: String): List<DepartmentDbModel>

    // ----------------------------------------------------------------------
    @Query("SELECT * FROM employees")
    suspend fun getEmployeeList(): List<EmployeeDbModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addEmployee(employeeDbModel: EmployeeDbModel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addEmployeeList(employeeDbModelList: List<EmployeeDbModel>)

    @Query("DELETE FROM employees")
    suspend fun deleteAllEmployees()

    @Query("SELECT * FROM employees WHERE id=:id LIMIT 1")
    suspend fun getEmployee(id: String): EmployeeDbModel?

    @Query("SELECT * FROM employees WHERE name LIKE :searchText")
    suspend fun searchEmployees(searchText: String): List<EmployeeDbModel>

    // ----------------------------------------------------------------------
    @Query("SELECT * FROM partners")
    suspend fun getPartnerList(): List<PartnerDbModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPartnerList(partnerDbModelList: List<PartnerDbModel>)

    @Query("DELETE FROM partners")
    suspend fun deleteAllPartners()

    @Query("SELECT * FROM partners WHERE id=:id LIMIT 1")
    suspend fun getPartner(id: String): PartnerDbModel?

    @Query("SELECT * FROM partners WHERE name LIKE :searchText OR inn LIKE :searchText")
    suspend fun searhPartners(searchText: String): List<PartnerDbModel>

    // ----------------------------------------------------------------------
    @Transaction
    @Query("SELECT * FROM cars WHERE ownerId=:ownerId ORDER BY name")
    suspend fun getCarsByOwner(ownerId: String): List<CarWithOwner>

    @Transaction
    @Query("SELECT * FROM cars")
    suspend fun getCarList(): List<CarWithOwner>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCarList(carDbModelList: List<CarDbModel>)

    @Query("DELETE FROM cars")
    suspend fun deleteAllCars()

    @Transaction
    @Query("SELECT * FROM cars WHERE id=:id LIMIT 1")
    suspend fun getCar(id: String): CarWithOwner?

    @Transaction
    @Query("SELECT * FROM cars WHERE ownerId=:ownerId AND (name LIKE :searchText OR manufacturer LIKE :searchText)")
    suspend fun searhCarsByOwner(searchText: String, ownerId: String): List<CarWithOwner>

    // ----------------------------------------------------------------------
    @Query("SELECT * FROM working_hours")
    suspend fun getWorkingHourList(): List<WorkingHourDbModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addWorkingHourList(partnerDbModelList: List<WorkingHourDbModel>)

    @Query("DELETE FROM working_hours")
    suspend fun deleteAllWorkingHours()

    @Query("SELECT * FROM working_hours WHERE id=:id LIMIT 1")
    suspend fun getWorkingHour(id: String): WorkingHourDbModel?

    @Query("SELECT * FROM working_hours WHERE name LIKE :searchText")
    suspend fun searhWorkingHours(searchText: String): List<WorkingHourDbModel>

    // --------------------------------------------------------------------------------
    @Query("DELETE FROM job_details WHERE workOrderId = :workOrderId")
    suspend fun deleteJobDetailsByWorkOrder(workOrderId: String)

    // --------------------------------------------------------------------------------
    @Query("DELETE FROM performer_details WHERE workOrderId = :workOrderId")
    suspend fun deletePerformersDetailsByWorkOrder(workOrderId: String)

    // ----------------------------------------
    @Query("SELECT * FROM default_work_order_settings LIMIT 1")
    suspend fun getDefaultWorkOrderSettings(): DefaultWorkOrderSettingsWithRequisities?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDefaultWorkOrderSettingsList(jobDetailDbModelList: List<DefaultWorkOrderSettingsDbModel>)
}
