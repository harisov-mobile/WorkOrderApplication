package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.car

import ru.internetcloud.workorderapplication.domain.repository.CarRepository

class DeleteCarListUseCase(private val carRepository: CarRepository) {
    suspend fun deleteAllCars() {
        return carRepository.deleteAllCars()
    }
}
