package ru.internetcloud.workorderapplication.data.datasource.remote

import javax.inject.Inject
import ru.internetcloud.workorderapplication.data.mapper.CarMapper
import ru.internetcloud.workorderapplication.data.network.api.ApiClient
import ru.internetcloud.workorderapplication.domain.catalog.Car

class CarRemoteDataSource @Inject constructor(
    private val carMapper: CarMapper
) {

    suspend fun getCarList(): List<Car> {
        val carResponse = ApiClient.getInstance().client.getCars()
        return carMapper.fromListDtoToListEntity(carResponse.cars)
    }
}
