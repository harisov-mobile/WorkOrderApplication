package ru.internetcloud.workorderapplication.data.repository

import android.app.Application
import ru.internetcloud.workorderapplication.data.database.AppDatabase
import ru.internetcloud.workorderapplication.data.entity.JobDetailDbModel
import ru.internetcloud.workorderapplication.data.entity.PerformerDetailDbModel
import ru.internetcloud.workorderapplication.data.entity.WorkOrderWithDetails
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

    suspend fun deleteAllJobDetails() {
        appDao.deleteAllJobDetails()
    }

    suspend fun deleteAllPerformers() {
        appDao.deleteAllPerformers()
    }

    suspend fun addJobDetailList(jobDetails: List<JobDetailDTO>) {

        val jobDetailDbModelList: MutableList<JobDetailDbModel> = mutableListOf()

        jobDetails.forEach { jobDetail ->
            jobDetailDbModelList.add(jobDetailMapper.fromDtoToDbModel(jobDetail))
        }

        appDao.addJobDetailList(jobDetailDbModelList)
    }

    suspend fun addPerformersList(performerDetails: List<PerformerDetailDTO>) {

        val performerDetailDbModelList: MutableList<PerformerDetailDbModel> = mutableListOf()

        performerDetails.forEach { performerDetail ->
            // вырезать идентификатор
            performerDetailDbModelList.add(performerDetailMapper.fromDtoToDbModel(performerDetail))
        }

        appDao.addPerformerDetailList(performerDetailDbModelList)
    }

    suspend fun addWorkOrderList(workOrders: List<WorkOrderDTO>) {
        appDao.addWorkOrderList(workOrderMapper.fromListDtoToListDboModel(workOrders))
    }

    suspend fun getModifiedWorkOrders(): List<WorkOrderWithDetails> {
        return appDao.getModifiedWorkOrders()
    }

    suspend fun getModifiedWorkOrdersQuantity(): Int {
        return appDao.getModifiedWorkOrders().size
    }
}
