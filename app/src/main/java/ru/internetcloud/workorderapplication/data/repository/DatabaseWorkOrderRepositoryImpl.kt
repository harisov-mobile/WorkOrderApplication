package ru.internetcloud.workorderapplication.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.room.Room
import ru.internetcloud.workorderapplication.data.database.AppDatabase
import ru.internetcloud.workorderapplication.data.mapper.WorkOrderMapper
import ru.internetcloud.workorderapplication.domain.document.WorkOrder
import ru.internetcloud.workorderapplication.domain.repository.WorkOrderRepository

// это класс-синглтон, единственный экземпляр этого репозитория создается при запуске приложения
class DatabaseWorkOrderRepositoryImpl private constructor(application: Application) : WorkOrderRepository {

    private val database: AppDatabase = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val workOrderDao = database.workOrderDao()
    private val workOrderMapper = WorkOrderMapper()

    companion object {
        private const val DATABASE_NAME = "work_order.db"

        private var instance: DatabaseWorkOrderRepositoryImpl? = null

        fun initialize(application: Application) {
            if (instance == null) {
                instance = DatabaseWorkOrderRepositoryImpl(application)
            }
        }

        fun get(): DatabaseWorkOrderRepositoryImpl {
            return instance ?: throw RuntimeException("DatabaseWorkOrderRepositoryImpl must be initialized.")
        }
    }

    override fun addWorkOrder(workOrder: WorkOrder) {
        workOrderDao.addWorkOrder(workOrderMapper.fromEntityToDbModel(workOrder))
    }

    override fun updateWorkOrder(workOrder: WorkOrder) {
        // т.к. onConflict = OnConflictStrategy.REPLACE, то это будет и UPDATE тоже
        workOrderDao.addWorkOrder(workOrderMapper.fromEntityToDbModel(workOrder))
    }

    override fun getWorkOrderList(): LiveData<List<WorkOrder>> {
        return Transformations.map(workOrderDao.getWorkOrderListLD()) {
            workOrderMapper.fromListDbModelToListEntity(it)
        }
    }

    override fun getWorkOrder(workOrderId: Int): WorkOrder? {
        var workOrder: WorkOrder? = null

        val workOrderDbModel = workOrderDao.getWorkOrder(workOrderId)

        workOrderDbModel?.let{
            workOrder = workOrderMapper.fromDbModelToEntity(it)
        }

        return workOrder
    }
}
