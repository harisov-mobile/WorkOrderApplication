package ru.internetcloud.workorderapplication.data.repository.db

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import ru.internetcloud.workorderapplication.data.database.AppDatabase
import ru.internetcloud.workorderapplication.data.mapper.WorkOrderMapper
import ru.internetcloud.workorderapplication.domain.document.WorkOrder
import ru.internetcloud.workorderapplication.domain.repository.WorkOrderRepository

class DbWorkOrderRepositoryImpl private constructor(application: Application) : WorkOrderRepository {

    private val workOrderDao = AppDatabase.getInstance(application).appDao()
    private val workOrderMapper = WorkOrderMapper()

    companion object {
        private var instance: DbWorkOrderRepositoryImpl? = null

        fun initialize(application: Application) {
            if (instance == null) {
                instance = DbWorkOrderRepositoryImpl(application)
            }
        }

        fun get(): DbWorkOrderRepositoryImpl {
            return instance ?: throw RuntimeException("DbWorkOrderRepositoryImpl must be initialized.")
        }
    }

    override suspend fun addWorkOrder(workOrder: WorkOrder) {
        workOrderDao.addWorkOrder(workOrderMapper.fromEntityToDbModel(workOrder))
    }

    override suspend fun updateWorkOrder(workOrder: WorkOrder) {
        // т.к. onConflict = OnConflictStrategy.REPLACE, то это будет и UPDATE тоже
        workOrderDao.addWorkOrder(workOrderMapper.fromEntityToDbModel(workOrder))
    }

    override fun getWorkOrderList(): LiveData<List<WorkOrder>> {
        return Transformations.map(workOrderDao.getWorkOrderList()) {
            workOrderMapper.fromListDbModelToListEntity(it)
        }
    }

    override suspend fun getWorkOrder(workOrderId: String): WorkOrder? {
        var workOrder: WorkOrder? = null

        val workOrderWithDetails = workOrderDao.getWorkOrder(workOrderId)

        workOrderWithDetails?.let {
            workOrder = workOrderMapper.fromDbModelToEntity(it)
        }

        return workOrder
    }
}
