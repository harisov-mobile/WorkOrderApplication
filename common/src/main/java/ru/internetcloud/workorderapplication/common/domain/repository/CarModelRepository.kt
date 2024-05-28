package ru.internetcloud.workorderapplication.common.domain.repository

import ru.internetcloud.workorderapplication.common.domain.model.catalog.CarModel

interface CarModelRepository {

    suspend fun getCarModelList(): List<CarModel>

    suspend fun addCarModelList(carModelList: List<CarModel>)

    suspend fun deleteAllCarModels()
}
