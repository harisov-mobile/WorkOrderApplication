package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.car

import ru.internetcloud.workorderapplication.domain.catalog.Car
import ru.internetcloud.workorderapplication.domain.repository.CarRepository

class GetCarUseCase(private val carRepository: CarRepository) {
    suspend fun getCar(id: String): Car? {
        return carRepository.getCar(id)
    }
}
