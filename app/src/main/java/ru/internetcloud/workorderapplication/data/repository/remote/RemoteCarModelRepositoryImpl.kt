package ru.internetcloud.workorderapplication.data.repository.remote

import ru.internetcloud.workorderapplication.data.mapper.CarModelMapper
import ru.internetcloud.workorderapplication.data.network.api.ApiClient
import ru.internetcloud.workorderapplication.domain.catalog.CarModel
import ru.internetcloud.workorderapplication.domain.repository.CarModelRepository
import javax.inject.Inject

class RemoteCarModelRepositoryImpl @Inject constructor(
    private val carModelMapper: CarModelMapper
) : CarModelRepository {

    override suspend fun getCarModelList(): List<CarModel> {
        val carModelResponse = ApiClient.getInstance().client.getCarModels() // для даггера
        return carModelMapper.fromListDtoToListEntity(carModelResponse.carModels)
    }

    override suspend fun addCarModelList(carModelList: List<CarModel>) {
        throw RuntimeException("Error - method addCarModelList is restricted in RemoteCarModelRepositoryImpl")
    }

    override suspend fun deleteAllCarModels() {
        throw RuntimeException("Error - method deleteAllCarModels is restricted in RemoteCarModelRepositoryImpl")
    }

}
