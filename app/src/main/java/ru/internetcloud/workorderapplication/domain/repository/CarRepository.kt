package ru.internetcloud.workorderapplication.domain.repository

import ru.internetcloud.workorderapplication.domain.catalog.Car

interface CarRepository {

    suspend fun getCarList(): List<Car>

    suspend fun getCarListByOwner(ownerId: String): List<Car>

    suspend fun addCarList(carList: List<Car>)

    suspend fun getCar(id: String): Car?

    suspend fun deleteAllCars()

    suspend fun searchCarsByOwner(searchText: String, ownerId: String): List<Car>
}
