package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.car

import ru.internetcloud.workorderapplication.domain.catalog.Car
import ru.internetcloud.workorderapplication.domain.repository.CarRepository

class AddCarListUseCase(private val carJobRepository: CarRepository) {
    suspend fun addCarList(carList: List<Car>) {
        return carJobRepository.addCarList(carList)
    }
}
