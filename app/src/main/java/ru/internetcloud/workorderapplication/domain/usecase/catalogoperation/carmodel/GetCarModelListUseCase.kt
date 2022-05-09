package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.carmodel

import ru.internetcloud.workorderapplication.domain.catalog.CarModel
import ru.internetcloud.workorderapplication.domain.repository.CarModelRepository

class GetCarModelListUseCase(private val carModelRepository: CarModelRepository) {
    suspend fun getCarModelList(): List<CarModel> {
        return carModelRepository.getCarModelList()
    }
}
