package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.carmodel

import javax.inject.Inject
import ru.internetcloud.workorderapplication.domain.catalog.CarModel
import ru.internetcloud.workorderapplication.domain.repository.CarModelRepository

class GetCarModelListUseCase @Inject constructor(
    private val carModelRepository: CarModelRepository
) {
    suspend fun getCarModelList(): List<CarModel> {
        return carModelRepository.getCarModelList()
    }
}
