package ru.internetcloud.workorderapplication.data.repository.common

import javax.inject.Inject
import ru.internetcloud.workorderapplication.data.datasource.local.CarModelLocalDataSource
import ru.internetcloud.workorderapplication.domain.catalog.CarModel
import ru.internetcloud.workorderapplication.domain.repository.CarModelRepository

class CarModelRepositoryImpl @Inject constructor(
    private val carModelLocalDataSource: CarModelLocalDataSource
) : CarModelRepository {

    override suspend fun getCarModelList(): List<CarModel> {
        return carModelLocalDataSource.getCarModelList()
    }

    override suspend fun addCarModelList(carModelList: List<CarModel>) {
        carModelLocalDataSource.addCarModelList(carModelList)
    }

    override suspend fun deleteAllCarModels() {
        carModelLocalDataSource.deleteAllCarModels()
    }
}
