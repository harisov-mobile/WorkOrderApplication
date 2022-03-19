package ru.internetcloud.workorderapplication.data.repository.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import ru.internetcloud.workorderapplication.data.database.AppDao
import ru.internetcloud.workorderapplication.data.mapper.JobDetailMapper
import ru.internetcloud.workorderapplication.data.mapper.PerformerDetailMapper
import ru.internetcloud.workorderapplication.data.mapper.WorkOrderMapper
import ru.internetcloud.workorderapplication.domain.document.WorkOrder
import ru.internetcloud.workorderapplication.domain.repository.WorkOrderRepository
import javax.inject.Inject

class DbWorkOrderRepositoryImpl @Inject constructor(
    // application: Application
    private val workOrderDao: AppDao,
    private val workOrderMapper: WorkOrderMapper,
    private val jobDetailMapper: JobDetailMapper,
    private val performerDetailMapper: PerformerDetailMapper
) : WorkOrderRepository {

    override suspend fun updateWorkOrder(workOrder: WorkOrder) {
        // т.к. onConflict = OnConflictStrategy.REPLACE, то это будет и UPDATE тоже
        workOrderDao.addWorkOrder(workOrderMapper.fromEntityToDbModel(workOrder))

        // сначала удалим ТЧ Работы, относящиеся к данному ордеру
        workOrderDao.deleteJobDetailsByWorkOrder(workOrder.id)
        // потом перезапишем Работы
        workOrderDao.addJobDetailList(
            jobDetailMapper.fromListEntityToListDbModel(
                list = workOrder.jobDetails,
                workOrderId = workOrder.id
            )
        )

        // сначала удалим ТЧ Исполнители, относящиеся к данному ордеру
        workOrderDao.deletePerformersDetailsByWorkOrder(workOrder.id)
        // потом перезапишем Исполнители
        workOrderDao.addPerformerDetailList(
            performerDetailMapper.fromListEntityToListDbModel(
                list = workOrder.performers,
                workOrderId = workOrder.id
            )
        )
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
