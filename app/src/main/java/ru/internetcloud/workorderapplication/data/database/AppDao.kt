package ru.internetcloud.workorderapplication.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.internetcloud.workorderapplication.data.entity.*

@Dao
interface AppDao {

    @Query("SELECT * FROM work_orders")
    fun getWorkOrderListLD(): LiveData<List<WorkOrderDbModel>> // Не использовать LiveData в репозитории

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addWorkOrder(workOrderDbModel: WorkOrderDbModel)

    @Query("SELECT * FROM work_orders WHERE id=:workOrderId LIMIT 1")
    suspend fun getWorkOrder(workOrderId: Int): WorkOrderDbModel // Андрей Сумин почему-то не Лив дату возвращает...

    // ----------------------------------------------------------------------
    @Query("SELECT * FROM repair_types")
    suspend fun getRepairTypeList(): List<RepairTypeDbModel> // Не использовать LiveData в репозитории

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRepairType(repairTypeDbModel: RepairTypeDbModel)

    @Query("DELETE FROM repair_types")
    suspend fun deleteAllRepairTypes()

    @Query("SELECT * FROM repair_types WHERE id=:id LIMIT 1")
    suspend fun getRepairType(id: String): RepairTypeDbModel?

    // ----------------------------------------------------------------------
    @Query("SELECT * FROM car_jobs")
    suspend fun getCarJobList(): List<CarJobDbModel> // Не использовать LiveData в репозитории

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCarJob(carJobDbModel: CarJobDbModel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCarJobList(carJobDbModelList: List<CarJobDbModel>)

    @Query("DELETE FROM car_jobs")
    suspend fun deleteAllCarJobs()

    @Query("SELECT * FROM car_jobs WHERE id=:id LIMIT 1")
    suspend fun getCarJob(id: String): CarJobDbModel?

    // ----------------------------------------------------------------------
    @Query("SELECT * FROM departments")
    suspend fun getDepartmentList(): List<DepartmentDbModel> // Не использовать LiveData в репозитории

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDepartment(departmentDbModel: DepartmentDbModel)

    @Query("DELETE FROM departments")
    suspend fun deleteAllDepartments()

    @Query("SELECT * FROM departments WHERE id=:id LIMIT 1")
    suspend fun getDepartment(id: String): DepartmentDbModel?

    // ----------------------------------------------------------------------
    @Query("SELECT * FROM employees")
    suspend fun getEmployeeList(): List<EmployeeDbModel> // Не использовать LiveData в репозитории

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addEmployee(employeeDbModel: EmployeeDbModel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addEmployeeList(employeeDbModelList: List<EmployeeDbModel>)

    @Query("DELETE FROM employees")
    suspend fun deleteAllEmployees()

    @Query("SELECT * FROM employees WHERE id=:id LIMIT 1")
    suspend fun getEmployee(id: String): EmployeeDbModel?

    // ----------------------------------------------------------------------
    @Query("SELECT * FROM partners")
    suspend fun getPartnerList(): List<PartnerDbModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPartnerList(partnerDbModelList: List<PartnerDbModel>)

    @Query("DELETE FROM partners")
    suspend fun deleteAllPartners()

    @Query("SELECT * FROM partners WHERE id=:id LIMIT 1")
    suspend fun getPartner(id: String): PartnerDbModel?
}
