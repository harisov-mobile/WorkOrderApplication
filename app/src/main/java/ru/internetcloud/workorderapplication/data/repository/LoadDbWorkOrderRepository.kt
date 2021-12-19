package ru.internetcloud.workorderapplication.data.repository

import android.app.Application
import ru.internetcloud.workorderapplication.data.database.AppDatabase
import ru.internetcloud.workorderapplication.data.entity.JobDetailDbModel
import ru.internetcloud.workorderapplication.data.entity.PerformerDetailDbModel
import ru.internetcloud.workorderapplication.data.entity.WorkOrderAndPerformerDetailCrossRef
import ru.internetcloud.workorderapplication.data.entity.WorkOrderAndJobDetailCrossRef
import ru.internetcloud.workorderapplication.data.mapper.JobDetailMapper
import ru.internetcloud.workorderapplication.data.mapper.PerformerDetailMapper
import ru.internetcloud.workorderapplication.data.mapper.WorkOrderMapper
import ru.internetcloud.workorderapplication.data.network.dto.JobDetailDTO
import ru.internetcloud.workorderapplication.data.network.dto.PerformerDetailDTO
import ru.internetcloud.workorderapplication.data.network.dto.WorkOrderDTO

class LoadDbWorkOrderRepository private constructor(application: Application) {

    private val appDao = AppDatabase.getInstance(application).appDao()
    private val jobDetailMapper = JobDetailMapper()
    private val performerDetailMapper = PerformerDetailMapper()
    private val workOrderMapper = WorkOrderMapper()

    companion object {

        private const val WORK_ORDER_ID_SIZE = 36

        private var instance: LoadDbWorkOrderRepository? = null

        fun initialize(application: Application) {
            if (instance == null) {
                instance = LoadDbWorkOrderRepository(application)
            }
        }

        fun get(): LoadDbWorkOrderRepository {
            return instance ?: throw RuntimeException("LoadDbWorkOrderRepository must be initialized.")
        }
    }

    suspend fun deleteAllWorkOrders() {
        appDao.deleteAllWorkOrders()
    }

    suspend fun deleteAllWorkOrderAndJobDetailCrossRefs() {
        appDao.deleteAllWorkOrderAndJobDetailCrossRefs()
    }

    suspend fun deleteAllWorkOrderAndEmployeeCrossRefs() {
        appDao.deleteAllWorkOrderAndEmployeeCrossRefs()
    }

    suspend fun addJobDetailList(jobDetails: List<JobDetailDTO>) {

        val listCrossRef: MutableList<WorkOrderAndJobDetailCrossRef> = mutableListOf()
        val jobDetailDbModelList: MutableList<JobDetailDbModel> = mutableListOf()

        jobDetails.forEach { jobDetail ->
            // вырезать из jobDetail.id id WorkOrder
            val workOrderId = jobDetail.id.substring(0, WORK_ORDER_ID_SIZE)
            listCrossRef.add(WorkOrderAndJobDetailCrossRef(workOrderId = workOrderId, jobDetailId = jobDetail.id))
            jobDetailDbModelList.add(jobDetailMapper.fromDtoToDbModel(jobDetail))
        }

        appDao.addJobDetailList(jobDetailDbModelList)
        appDao.addWorkOrderAndJobDetailCrossRefList(listCrossRef)
    }

    suspend fun addPerformersList(performerDetails: List<PerformerDetailDTO>) {

        val listCrossRef: MutableList<WorkOrderAndPerformerDetailCrossRef> = mutableListOf()
        val performerDetailDbModelList: MutableList<PerformerDetailDbModel> = mutableListOf()

        performerDetails.forEach { performerDetail ->
            // вырезать идентификатор
            val workOrderId = performerDetail.id.substring(0, WORK_ORDER_ID_SIZE)
            listCrossRef.add(WorkOrderAndPerformerDetailCrossRef(workOrderId = workOrderId, performerDetailId = performerDetail.id))
            performerDetailDbModelList.add(performerDetailMapper.fromDtoToDbModel(performerDetail))
        }

        appDao.addPerformerDetailList(performerDetailDbModelList)
        appDao.addWorkOrderAndPerformerDetailCrossRefList(listCrossRef)
    }

    suspend fun addWorkOrderList(workOrders: List<WorkOrderDTO>) {
        appDao.addWorkOrderList(workOrderMapper.fromListDtoToListDboModel(workOrders))
    }
}