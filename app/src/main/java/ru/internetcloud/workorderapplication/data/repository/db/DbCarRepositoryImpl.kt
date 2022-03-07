package ru.internetcloud.workorderapplication.data.repository.db

import ru.internetcloud.workorderapplication.data.database.AppDao
import ru.internetcloud.workorderapplication.data.mapper.CarMapper
import ru.internetcloud.workorderapplication.domain.catalog.Car
import ru.internetcloud.workorderapplication.domain.repository.CarRepository
import javax.inject.Inject

class DbCarRepositoryImpl @Inject constructor(
    // private val application: Application,
    private val appDao: AppDao,
    private val carMapper: CarMapper
) : CarRepository {

    companion object {
//        private var instance: DbCarRepositoryImpl? = null
//
//        fun initialize(application: Application) {
//            if (instance == null) {
//                instance = DbCarRepositoryImpl(application)
//            }
//        }
//
//        fun get(): DbCarRepositoryImpl {
//            return instance ?: throw RuntimeException("DbCarModelRepositoryImpl must be initialized.")
//        }
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

    override suspend fun searchCarsByOwner(searchText: String, ownerId: String): List<Car> {
        return carMapper.fromListCarWithOwnerToListEntity(appDao.searhCarsByOwner("%$searchText%", ownerId))
    }
}
