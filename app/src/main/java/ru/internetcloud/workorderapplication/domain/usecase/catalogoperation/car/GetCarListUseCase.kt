package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.car

import ru.internetcloud.workorderapplication.domain.catalog.Car
import ru.internetcloud.workorderapplication.domain.repository.CarRepository

class GetCarListUseCase(private val carRepository: CarRepository) {
    suspend fun getCarList(): List<Car> {
        return carRepository.getCarList()
    }
}
