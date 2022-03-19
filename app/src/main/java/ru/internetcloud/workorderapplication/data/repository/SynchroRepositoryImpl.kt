package ru.internetcloud.workorderapplication.data.repository

import android.util.Log
import ru.internetcloud.workorderapplication.data.database.AppDao
import ru.internetcloud.workorderapplication.data.entity.*
import ru.internetcloud.workorderapplication.data.mapper.*
import ru.internetcloud.workorderapplication.data.network.api.ApiClient
import ru.internetcloud.workorderapplication.data.network.dto.*
import ru.internetcloud.workorderapplication.domain.common.FunctionResult
import ru.internetcloud.workorderapplication.domain.common.SendRequest
import ru.internetcloud.workorderapplication.domain.repository.SynchroRepository
import javax.inject.Inject

class SynchroRepositoryImpl @Inject constructor(
    private val appDao: AppDao,
    private val jobDetailMapper: JobDetailMapper,
    private val performerDetailMapper: PerformerDetailMapper,
    private val workOrderMapper: WorkOrderMapper,
    private val defaultWorkOrderSettingsMapper: DefaultWorkOrderSettingsMapper,
    private val defaultRepairTypeJobDetailMapper: DefaultRepairTypeJobDetailMapper,
    private val repairTypeMapper: RepairTypeMapper
) : SynchroRepository {

    override suspend fun loadWorkOrders() {

        deleteAllJobDetails()
        deleteAllPerformers()
        deleteAllWorkOrders()

        val workOrderResponse = ApiClient.getInstance().client.getWorkOrders()

        addJobDetailListfromDTO(workOrderResponse.jobDetails)

        addPerformersList(workOrderResponse.performerDetails)

        addWorkOrderList(workOrderResponse.workOrders)
    }

    override suspend fun loadRepairTypes() {

        deleteAllDefaultRepairTypeJobDetails()

        val repairTypeResponse = ApiClient.getInstance().client.getRepairTypes()

        addDefauiltRepairJobDetailListFromDTO(repairTypeResponse.defaultJobDetails)

        addRepairTypeList(repairTypeResponse.repairTypes)
    }

    override suspend fun deleteAllDefaultRepairTypeJobDetails() {
        appDao.deleteAllDefaultRepairTypeJobDetails()
    }

    suspend fun addDefauiltRepairJobDetailListFromDTO(defaultJobDetails: List<DefaultRepairTypeJobDetailDTO>) {

        val defaultJobDetailDbModelList: MutableList<DefaultRepairTypeJobDetailDbModel> = mutableListOf()

        defaultJobDetails.forEach { jobDetail ->
            defaultJobDetailDbModelList.add(defaultRepairTypeJobDetailMapper.fromDtoToDbModel(jobDetail))
        }

        appDao.addDefaultRepairTypeJobDetailList(defaultJobDetailDbModelList)
    }

    suspend fun addRepairTypeList(repairTypes: List<RepairTypeDTO>) {
        appDao.addRepairTypeList(repairTypeMapper.fromListDtoToListDboModel(repairTypes))
    }

    override suspend fun loadDefaultWorkOrderSettings() {
        val defResponse = ApiClient.getInstance().client.getDefaultWorkOrderSettings()
        deleteAllDefaultWorkOrderSettings()
        addDefaultWorkOrderSettingsList(defResponse.settings)
    }

    override suspend fun sendWorkOrderToEmail(id: String, email: String): FunctionResult {

        val result = FunctionResult()

        val sendRequest = SendRequest(id = id, email = email)

        try {
            val uploadResponse = ApiClient.getInstance().client.sendWorkOrderToEmail(sendRequest)
            Log.i("rustam", "id={$id} после  ApiClient.getInstance().client.sendWorkOrderToEmail()")
            if (uploadResponse.uploadResult.isSuccess) {
                result.isSuccess = true
            } else {
                result.errorMessage = uploadResponse.uploadResult.errorMessage
            }
        } catch (e: Exception) {
            // ничего не делаю
            Log.i("rustam", "ошибка при отправке заказ-наряда на эл.почту по id: " + e.toString())
            result.errorMessage = e.toString()
        }

        return result
    }

    override suspend fun uploadWorkOrders(): FunctionResult {

        val result = FunctionResult()

        val listWO = getModifiedWorkOrders()
        if (listWO.isEmpty()) {
            result.isSuccess = true
        } else {
            result.amountOfModifiedWorkOrders = listWO.size

            try {
                val uploadWorkOrderResponse = ApiClient.getInstance().client.uploadWorkOrders(listWO)
                if (uploadWorkOrderResponse.uploadResult.isSuccess) {
                    result.isSuccess = true
                } else {
                    result.errorMessage = uploadWorkOrderResponse.uploadResult.errorMessage
                }
            } catch (e: Exception) {
                // ничего не делаю
                // Log.i("rustam", "ошибка при выгрузке заказ-нарядов: " + e.toString())
                result.errorMessage = e.toString()
            }
        }

        return result
    }

    override suspend fun uploadWorkOrderById(id: String): FunctionResult {
        val result = FunctionResult()

        val modifiedWorkOrder: WorkOrderWithDetails? = getModifiedWorkOrderById(id)
        if (modifiedWorkOrder == null) {
            // не надо отправлять заказ-наряд на сервер 1С. так как в него не вносились изменения
            result.isSuccess = true
        } else {
            Log.i("rustam", "начинаем uploadWorkOrderById")

            result.amountOfModifiedWorkOrders = 1

            val listWO: MutableList<WorkOrderWithDetails> = mutableListOf()
            listWO.add(modifiedWorkOrder)

            try {
                val uploadWorkOrderResponse = ApiClient.getInstance().client.uploadWorkOrders(listWO)
                Log.i("rustam", "id={$id} после  ApiClient.getInstance().client.uploadWorkOrders()")
                if (uploadWorkOrderResponse.uploadResult.isSuccess) {
                    result.isSuccess = true
                } else {
                    result.errorMessage = uploadWorkOrderResponse.uploadResult.errorMessage
                }
            } catch (e: Exception) {
                // ничего не делаю
                Log.i("rustam", "ошибка при выгрузке заказ-наряда по id: " + e.toString())
                result.errorMessage = e.toString()
            }
        }

        return result
    }

    override suspend fun deleteAllWorkOrders() {
        appDao.deleteAllWorkOrders()
    }

    override suspend fun deleteAllJobDetails() {
        appDao.deleteAllJobDetails()
    }

    suspend fun deleteAllDefaultWorkOrderSettings() {
        appDao.deleteAllDefaultWorkOrderSettings()
    }

    override suspend fun deleteAllPerformers() {
        appDao.deleteAllPerformers()
    }

    suspend fun addJobDetailListfromDTO(jobDetails: List<JobDetailDTO>) {

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

    suspend fun getModifiedWorkOrderById(id: String): WorkOrderWithDetails? {
        return appDao.getModifiedWorkOrderById(id)
    }

    override suspend fun getModifiedWorkOrdersQuantity(): Int {
        return appDao.getModifiedWorkOrders().size
    }
}
