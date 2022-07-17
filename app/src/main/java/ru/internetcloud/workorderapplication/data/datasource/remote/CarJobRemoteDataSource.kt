package ru.internetcloud.workorderapplication.data.datasource.remote

import javax.inject.Inject
import ru.internetcloud.workorderapplication.data.mapper.CarJobMapper
import ru.internetcloud.workorderapplication.data.network.api.ApiClient
import ru.internetcloud.workorderapplication.domain.catalog.CarJob

class CarJobRemoteDataSource @Inject constructor(
    private val carJobMapper: CarJobMapper
) {

    suspend fun getCarJobList(): List<CarJob> {
        val carJobResponse = ApiClient.getInstance().client.getCarJobs()
        return carJobMapper.fromListDtoToListEntity(carJobResponse.carJobs)
    }
}
