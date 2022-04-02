package ru.internetcloud.workorderapplication.domain.repository

import ru.internetcloud.workorderapplication.domain.catalog.CarModel

interface CarModelRepository {

    suspend fun getCarModelList(): List<CarModel>

    suspend fun addCarModelList(carModelList: List<CarModel>)

    suspend fun deleteAllCarModels()
}
