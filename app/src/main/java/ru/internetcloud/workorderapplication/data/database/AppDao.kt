package ru.internetcloud.workorderapplication.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.internetcloud.workorderapplication.data.entity.CarJobDbModel
import ru.internetcloud.workorderapplication.data.entity.RepairTypeDbModel
import ru.internetcloud.workorderapplication.data.entity.WorkOrderDbModel

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

    @Query("DELETE FROM car_jobs")
    suspend fun deleteAllCarJobs()

    @Query("SELECT * FROM car_jobs WHERE id=:id LIMIT 1")
    suspend fun getCarJob(id: String): CarJobDbModel?
}
