package ru.internetcloud.workorderapplication.data.datasource.remote

import javax.inject.Inject
import ru.internetcloud.workorderapplication.data.model.WorkOrderWithDetails
import ru.internetcloud.workorderapplication.data.network.api.ApiClient
import ru.internetcloud.workorderapplication.data.network.dto.UploadResponse
import ru.internetcloud.workorderapplication.data.network.dto.WorkOrderResponse

class WorkOrderRemoteDataSource @Inject constructor() {

    suspend fun getWorkOrders(): WorkOrderResponse {
        return ApiClient.getInstance().client.getWorkOrders()
    }

    suspend fun uploadWorkOrders(listWO: List<WorkOrderWithDetails>): UploadResponse {
        return ApiClient.getInstance().client.uploadWorkOrders(listWO)
    }
}
