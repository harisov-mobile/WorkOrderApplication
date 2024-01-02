package ru.internetcloud.workorderapplication.common.data.datasource.remote

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.data.mapper.CarModelMapper
import ru.internetcloud.workorderapplication.common.data.network.api.ApiClient
import ru.internetcloud.workorderapplication.common.domain.model.catalog.CarModel

class CarModelRemoteDataSource @Inject constructor(
    private val carModelMapper: CarModelMapper
) {

    suspend fun getCarModelList(): List<CarModel> {
        val carModelResponse = ApiClient.getInstance().client.getCarModels()
        return carModelMapper.fromListDtoToListEntity(carModelResponse.carModels)
    }
}
