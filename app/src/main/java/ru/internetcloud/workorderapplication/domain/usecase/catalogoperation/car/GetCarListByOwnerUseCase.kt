package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.car

import ru.internetcloud.workorderapplication.domain.model.catalog.Car
import ru.internetcloud.workorderapplication.domain.repository.CarRepository
import javax.inject.Inject

class GetCarListByOwnerUseCase @Inject constructor(
    private val carRepository: CarRepository
) {

    suspend fun getCarListByOwner(ownerId: String): List<Car> {
        return carRepository.getCarListByOwner(ownerId)
    }
}
