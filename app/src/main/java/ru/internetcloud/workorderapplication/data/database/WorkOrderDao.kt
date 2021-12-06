package ru.internetcloud.workorderapplication.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.internetcloud.workorderapplication.data.entity.WorkOrderDbModel
import ru.internetcloud.workorderapplication.domain.document.WorkOrder

@Dao
interface WorkOrderDao {

    @Query("SELECT * FROM work_orders")
    fun getWorkOrderListLD() : LiveData<List<WorkOrder>> // Не использовать LiveData в репозитории

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addWorkOrder(workOrderDbModel: WorkOrderDbModel)

    @Query("SELECT * FROM work_orders WHERE id=:workOrderId LIMIT 1")
    fun getWorkOrderLD(workOrderId: String) : LiveData<WorkOrder> // Не использовать LiveData в репозитории

}