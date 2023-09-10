package ru.internetcloud.workorderapplication.data.datasource.remote

import ru.internetcloud.workorderapplication.data.mapper.CarModelMapper
import ru.internetcloud.workorderapplication.data.network.api.ApiClient
import ru.internetcloud.workorderapplication.domain.model.catalog.CarModel
import javax.inject.Inject

class CarModelRemoteDataSource @Inject constructor(
    private val carModelMapper: CarModelMapper
) {

    suspend fun getCarModelList(): List<CarModel> {
        val carModelResponse = ApiClient.getInstance().client.getCarModels()
        return carModelMapper.fromListDtoToListEntity(carModelResponse.carModels)
    }
}
