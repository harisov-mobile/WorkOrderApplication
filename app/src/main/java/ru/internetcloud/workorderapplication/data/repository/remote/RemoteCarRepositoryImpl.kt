package ru.internetcloud.workorderapplication.data.repository.remote

import android.util.Log
import ru.internetcloud.workorderapplication.data.mapper.CarMapper
import ru.internetcloud.workorderapplication.data.network.api.ApiClient
import ru.internetcloud.workorderapplication.data.network.dto.CarResponse
import ru.internetcloud.workorderapplication.domain.catalog.Car
import ru.internetcloud.workorderapplication.domain.repository.CarRepository

class RemoteCarRepositoryImpl private constructor() : CarRepository {

    companion object {
        private var instance: RemoteCarRepositoryImpl? = null

        fun initialize() {
            if (instance == null) {
                instance = RemoteCarRepositoryImpl()
            }
        }

        fun get(): RemoteCarRepositoryImpl {
            return instance ?: throw RuntimeException("RemoteCarRepositoryImpl must be initialized.")
        }
    }

    private val carMapper = CarMapper()

    override suspend fun getCarList(): List<Car> {
        var carResponse = CarResponse(emptyList())

        try {
            carResponse = ApiClient.getInstance().client.getCars()
        } catch (e: Exception) {
            // ничего не делаю
            Log.i("rustam", "ошибка при загрузке getCars" + e.toString())
        }

        return carMapper.fromListDtoToListEntity(carResponse.cars)
    }

    override suspend fun getCarListByOwner(ownerId: String): List<Car> {
        throw RuntimeException("Error - method getCarsByOwner is restricted in RemoteCarModelRepositoryImpl")
    }

    override suspend fun addCarList(carList: List<Car>) {
        throw RuntimeException("Error - method addCarList is restricted in RemoteCarModelRepositoryImpl")
    }

    override suspend fun getCar(id: String): Car? {
        throw RuntimeException("Error - method getCar is restricted in RemoteCarModelRepositoryImpl")
        return null
    }

    override suspend fun deleteAllCars() {
        throw RuntimeException("Error - method deleteAllCars is restricted in RemoteCarModelRepositoryImpl")
    }

    override suspend fun searchCars(searchText: String): List<Car> {
        throw RuntimeException("Error - method searchCars is restricted in RemoteCarModelRepositoryImpl")
    }
}
