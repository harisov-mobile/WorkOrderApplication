package ru.internetcloud.workorderapplication.data.repository.common

import ru.internetcloud.workorderapplication.data.datasource.local.CarLocalDataSource
import ru.internetcloud.workorderapplication.domain.model.catalog.Car
import ru.internetcloud.workorderapplication.domain.repository.CarRepository
import javax.inject.Inject

class CarRepositoryImpl @Inject constructor(
    private val carLocalDataSource: CarLocalDataSource
) : CarRepository {

    override suspend fun getCarList(): List<Car> {
        return carLocalDataSource.getCarList()
    }

    override suspend fun getCarListByOwner(ownerId: String): List<Car> {
        return carLocalDataSource.getCarListByOwner(ownerId)
    }

    override suspend fun addCarList(carList: List<Car>) {
        carLocalDataSource.addCarList(carList)
    }

    override suspend fun deleteAllCars() {
        carLocalDataSource.deleteAllCars()
    }

    override suspend fun searchCarsByOwner(searchText: String, ownerId: String): List<Car> {
        return carLocalDataSource.searchCarsByOwner(searchText, ownerId)
    }
}
