package ru.internetcloud.workorderapplication.common.data.datasource.remote

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.data.mapper.CarJobMapper
import ru.internetcloud.workorderapplication.common.data.network.api.ApiClient
import ru.internetcloud.workorderapplication.common.domain.model.catalog.CarJob

class CarJobRemoteDataSource @Inject constructor(
    private val carJobMapper: CarJobMapper
) {

    suspend fun getCarJobList(): List<CarJob> {
        val carJobResponse = ApiClient.getInstance().client.getCarJobs()
        return carJobMapper.fromListDtoToListEntity(carJobResponse.carJobs)
    }
}
