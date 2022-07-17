package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.car

import javax.inject.Inject
import ru.internetcloud.workorderapplication.domain.catalog.Car
import ru.internetcloud.workorderapplication.domain.repository.CarRepository

class GetCarListByOwnerUseCase @Inject constructor(
    private val carRepository: CarRepository
) {

    suspend fun getCarListByOwner(ownerId: String): List<Car> {
        return carRepository.getCarListByOwner(ownerId)
    }
}
