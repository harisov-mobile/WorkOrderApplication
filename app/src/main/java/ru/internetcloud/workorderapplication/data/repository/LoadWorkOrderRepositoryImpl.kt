package ru.internetcloud.workorderapplication.data.repository

import android.app.Application
import android.util.Log
import ru.internetcloud.workorderapplication.data.network.api.ApiClient
import ru.internetcloud.workorderapplication.data.network.dto.WorkOrderResponse
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

        Log.i("rustam", "начинаем loadWorkOrderList")

        var success = false

        var workOrderResponse = WorkOrderResponse(emptyList(), emptyList(), emptyList())

        try {
            workOrderResponse = ApiClient.getInstance().client.getWorkOrders()
            Log.i("rustam", "после  ApiClient.getInstance().client.getWorkOrders()")
            success = true
        } catch (e: Exception) {
            // ничего не делаю
            Log.i("rustam", "ошибка при загрузке заказ-нарядов" + e.toString())
        }

        if (success) {
            loadDbWorkOrderRepository.deleteAllWorkOrderAndJobDetailCrossRefs()
            loadDbWorkOrderRepository.addJobDetailList(workOrderResponse.jobDetails)

            loadDbWorkOrderRepository.deleteAllWorkOrderAndEmployeeCrossRefs()
            loadDbWorkOrderRepository.addPerformersList(workOrderResponse.performerDetails)



            loadDbWorkOrderRepository.deleteAllWorkOrders()

            Log.i("rustam", "после  loadDbWorkOrderRepository.deleteAllWorkOrders()")
            Log.i("rustam", "workOrderResponse.workOrders = " + workOrderResponse.workOrders.toString())

            loadDbWorkOrderRepository.addWorkOrderList(workOrderResponse.workOrders)
        }

        return success
    }

}
