package ru.internetcloud.workorderapplication.data.datasource.local

import javax.inject.Inject
import ru.internetcloud.workorderapplication.data.database.AppDao
import ru.internetcloud.workorderapplication.data.mapper.CarMapper
import ru.internetcloud.workorderapplication.domain.catalog.Car

class CarLocalDataSource @Inject constructor(
    private val appDao: AppDao,
    private val carMapper: CarMapper
) {

    suspend fun getCarList(): List<Car> {
        return carMapper.fromListCarWithOwnerToListEntity(appDao.getCarList())
    }

    suspend fun getCarListByOwner(ownerId: String): List<Car> {
        return carMapper.fromListCarWithOwnerToListEntity(appDao.getCarsByOwner(ownerId))
    }

    suspend fun addCarList(carList: List<Car>) {
        appDao.addCarList(carMapper.fromListEntityToListDbModel(carList))
    }

    suspend fun deleteAllCars() {
        appDao.deleteAllCars()
    }

    suspend fun searchCarsByOwner(searchText: String, ownerId: String): List<Car> {
        return carMapper.fromListCarWithOwnerToListEntity(appDao.searhCarsByOwner("%$searchText%", ownerId))
    }
}
