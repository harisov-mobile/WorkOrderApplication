package ru.internetcloud.workorderapplication.data.repository

import android.app.Application
import android.util.Log
import ru.internetcloud.workorderapplication.data.database.AppDatabase
import ru.internetcloud.workorderapplication.data.entity.DefaultWorkOrderSettingsDbModel
import ru.internetcloud.workorderapplication.data.entity.JobDetailDbModel
import ru.internetcloud.workorderapplication.data.entity.PerformerDetailDbModel
import ru.internetcloud.workorderapplication.data.entity.WorkOrderWithDetails
import ru.internetcloud.workorderapplication.data.mapper.DefaultWorkOrderSettingsMapper
import ru.internetcloud.workorderapplication.data.mapper.JobDetailMapper
import ru.internetcloud.workorderapplication.data.mapper.PerformerDetailMapper
import ru.internetcloud.workorderapplication.data.mapper.WorkOrderMapper
import ru.internetcloud.workorderapplication.data.network.api.ApiClient
import ru.internetcloud.workorderapplication.data.network.dto.*
import ru.internetcloud.workorderapplication.domain.common.FunctionResult
import ru.internetcloud.workorderapplication.domain.document.WorkOrder
import ru.internetcloud.workorderapplication.domain.repository.SynchroRepository

class SynchroRepositoryImpl private constructor(application: Application) : SynchroRepository {

    private val appDao = AppDatabase.getInstance(application).appDao()
    private val jobDetailMapper = JobDetailMapper()
    private val performerDetailMapper = PerformerDetailMapper()
    private val workOrderMapper = WorkOrderMapper()
    private val defaultWorkOrderSettingsMapper = DefaultWorkOrderSettingsMapper()

    companion object {
        private var instance: SynchroRepositoryImpl? = null

        fun initialize(application: Application) {
            if (instance == null) {
                instance = SynchroRepositoryImpl(application)
            }
        }

        fun get(): SynchroRepositoryImpl {
            return instance ?: throw RuntimeException("SynchroRepositoryImpl must be initialized.")
        }
    }

    override suspend fun loadWorkOrders(): Boolean {

        var success = false

        var workOrderResponse = WorkOrderResponse(emptyList(), emptyList(), emptyList())

        try {
            workOrderResponse = ApiClient.getInstance().client.getWorkOrders()
            success = true
        } catch (e: Exception) {
            // ничего не делаю
            Log.i("rustam", "ошибка при загрузке заказ-нарядов" + e.toString())
        }

        if (success) {
            deleteAllJobDetails()
            addJobDetailList(workOrderResponse.jobDetails)

            deleteAllPerformers()
            addPerformersList(workOrderResponse.performerDetails)

            deleteAllWorkOrders()
            addWorkOrderList(workOrderResponse.workOrders)
        }
        return success
    }

    override suspend fun loadDefaultWorkOrderSettings(): Boolean {
        var success = false

        var defResponse = DefaultWorkOrderSettingsResponse(emptyList())

        try {
            defResponse = ApiClient.getInstance().client.getDefaultWorkOrderSettings()
            success = true
        } catch (e: Exception) {
            // ничего не делаю
            Log.i("rustam", "ошибка при загрузке настроек заполнения заказ-наряда" + e.toString())
        }

        if (success) {
            deleteAllDefaultWorkOrderSettings()
            addDefaultWorkOrderSettingsList(defResponse.settings)
        }
        return success
    }

    override suspend fun sendWorkOrderByEmail(workOrder: WorkOrder): FunctionResult {
        TODO("Not yet implemented")
    }

    override suspend fun uploadWorkOrders(): FunctionResult {

        val result = FunctionResult()

        val listWO = getModifiedWorkOrders()
        if (listWO.isEmpty()) {
            result.isSuccess = true
        } else {
            Log.i("rustam", "начинаем uploadWorkOrderList")

            result.amountOfModifiedWorkOrders = listWO.size

            try {
                val uploadWorkOrderResponse = ApiClient.getInstance().client.uploadWorkOrders(listWO)
                Log.i("rustam", "после  ApiClient.getInstance().client.uploadWorkOrders()")
                if (uploadWorkOrderResponse.uploadResult.isSuccess) {
                    result.isSuccess = true
                } else {
                    result.errorMessage = uploadWorkOrderResponse.uploadResult.errorMessage
                }
            } catch (e: Exception) {
                // ничего не делаю
                Log.i("rustam", "ошибка при выгрузке заказ-нарядов" + e.toString())
                result.errorMessage = e.toString()
            }
        }

        return result
    }

    override suspend fun uploadWorkOrderById(id: String): FunctionResult {
        TODO("Not yet implemented")
    }

    suspend fun deleteAllWorkOrders() {
        appDao.deleteAllWorkOrders()
    }

    suspend fun deleteAllJobDetails() {
        appDao.deleteAllJobDetails()
    }

    suspend fun deleteAllDefaultWorkOrderSettings() {
        appDao.deleteAllDefaultWorkOrderSettings()
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

    suspend fun addDefaultWorkOrderSettingsList(defDTOList: List<DefaultWorkOrderSettingsDTO>) {

        val defDbModelList: MutableList<DefaultWorkOrderSettingsDbModel> = mutableListOf()

        defDTOList.forEach { defDTO ->
            defDbModelList.add(defaultWorkOrderSettingsMapper.fromDtoToDbModel(defDTO))
        }

        appDao.addDefaultWorkOrderSettingsList(defDbModelList)
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

    override suspend fun getModifiedWorkOrdersQuantity(): Int {
        return appDao.getModifiedWorkOrders().size
    }
}
