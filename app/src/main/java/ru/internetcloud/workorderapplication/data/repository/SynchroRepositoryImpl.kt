package ru.internetcloud.workorderapplication.data.repository

import android.util.Log
import ru.internetcloud.workorderapplication.data.network.api.ApiClient
import ru.internetcloud.workorderapplication.data.network.dto.WorkOrderResponse
import ru.internetcloud.workorderapplication.domain.common.FunctionResult
import ru.internetcloud.workorderapplication.domain.repository.SynchroRepository

class SynchroRepositoryImpl private constructor(
    private val loadDbWorkOrderRepository: LoadDbWorkOrderRepository
) : SynchroRepository {

    companion object {
        private var instance: SynchroRepositoryImpl? = null

        fun initialize(loadDbWorkOrderRepository: LoadDbWorkOrderRepository) {
            if (instance == null) {
                instance = SynchroRepositoryImpl(loadDbWorkOrderRepository)
            }
        }

        fun get(): SynchroRepositoryImpl {
            return instance ?: throw RuntimeException("SynchroRepositoryImpl must be initialized.")
        }
    }

    override suspend fun getModifiedWorkOrdersQuantity(): Int {
        return loadDbWorkOrderRepository.getModifiedWorkOrdersQuantity()
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
            loadDbWorkOrderRepository.deleteAllJobDetails()
            loadDbWorkOrderRepository.addJobDetailList(workOrderResponse.jobDetails)

            loadDbWorkOrderRepository.deleteAllPerformers()
            loadDbWorkOrderRepository.addPerformersList(workOrderResponse.performerDetails)

            loadDbWorkOrderRepository.deleteAllWorkOrders()
            loadDbWorkOrderRepository.addWorkOrderList(workOrderResponse.workOrders)
        }
        return success
    }

    override suspend fun uploadWorkOrders(): FunctionResult {

        val result = FunctionResult()

        val listWO = loadDbWorkOrderRepository.getModifiedWorkOrders()
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
}
