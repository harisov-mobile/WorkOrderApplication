package ru.internetcloud.workorderapplication.data.repository.db

import android.app.Application
import ru.internetcloud.workorderapplication.data.database.AppDatabase
import ru.internetcloud.workorderapplication.data.mapper.CarMapper
import ru.internetcloud.workorderapplication.domain.catalog.Car
import ru.internetcloud.workorderapplication.domain.repository.CarRepository

class DbCarRepositoryImpl private constructor(application: Application) : CarRepository {

    private val appDao = AppDatabase.getInstance(application).appDao()
    private val carMapper = CarMapper()

    companion object {
        private var instance: DbCarRepositoryImpl? = null

        fun initialize(application: Application) {
            if (instance == null) {
                instance = DbCarRepositoryImpl(application)
            }
        }

        fun get(): DbCarRepositoryImpl {
            return instance ?: throw RuntimeException("DbCarModelRepositoryImpl must be initialized.")
        }
    }

    override suspend fun getCarList(): List<Car> {
        return carMapper.fromListCarWithOwnerToListEntity(appDao.getCarList())
    }

    override suspend fun getCarListByOwner(ownerId: String): List<Car> {
        return carMapper.fromListCarWithOwnerToListEntity(appDao.getCarsByOwner(ownerId))
    }

    override suspend fun addCarList(carList: List<Car>) {
        appDao.addCarList(carMapper.fromListEntityToListDbModel(carList))
    }

    override suspend fun getCar(id: String): Car? {
        var car: Car? = null

        val carWithOwner = appDao.getCar(id)

        carWithOwner?.let {
            car = carMapper.fromCarWithOwnerToEntity(it)
        }

        return car
    }

    override suspend fun deleteAllCars() {
        appDao.deleteAllCars()
    }

    override suspend fun searchCars(searchText: String): List<Car> {
        return carMapper.fromListCarWithOwnerToListEntity(appDao.searhCars("%$searchText%"))
    }
}
