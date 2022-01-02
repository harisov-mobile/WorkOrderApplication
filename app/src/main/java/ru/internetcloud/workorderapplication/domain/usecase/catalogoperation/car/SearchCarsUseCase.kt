package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.car

import ru.internetcloud.workorderapplication.domain.catalog.Car
import ru.internetcloud.workorderapplication.domain.repository.CarRepository

class SearchCarsUseCase(private val carRepository: CarRepository) {
    suspend fun searchCars(searchText: String): List<Car> {
        return carRepository.searchCars(searchText)
    }
}
