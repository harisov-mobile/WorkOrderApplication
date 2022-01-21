package ru.internetcloud.workorderapplication.data.repository

import android.app.Application
import android.util.Log
import ru.internetcloud.workorderapplication.data.network.api.ApiClient
import ru.internetcloud.workorderapplication.data.network.dto.WorkOrderResponse
import ru.internetcloud.workorderapplication.domain.common.FunctionResult
import ru.internetcloud.workorderapplication.domain.repository.LoadWorkOrderRepository

class LoadWorkOrderRepositoryImpl private constructor(
    application: Application,
    private val loadDbWorkOrderRepository: LoadDbWorkOrderRepository
) : LoadWorkOrderRepository {

    companion object {
        private var instance: LoadWorkOrderRepositoryImpl? = null

        fun initialize(application: Application, loadDbWorkOrderRepository: LoadDbWorkOrderRepository) {
            if (instance == null) {
                instance = LoadWorkOrderRepositoryImpl(application, loadDbWorkOrderRepository)
            }
        }

        fun get(): LoadWorkOrderRepositoryImpl {
            return instance ?: throw RuntimeException("LoadWorkOrderRepositoryImpl must be initialized.")
        }
    }

    override suspend fun loadWorkOrderList(): Boolean {

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

    override suspend fun uploadWorkOrderList(): FunctionResult {

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
