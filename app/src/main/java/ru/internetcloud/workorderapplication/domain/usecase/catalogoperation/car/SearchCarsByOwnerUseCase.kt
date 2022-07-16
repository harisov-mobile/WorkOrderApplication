package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.car

import javax.inject.Inject
import ru.internetcloud.workorderapplication.domain.catalog.Car
import ru.internetcloud.workorderapplication.domain.repository.CarRepository

class SearchCarsByOwnerUseCase @Inject constructor(
    private val carRepository: CarRepository
) {
    suspend fun searchCarsByOwner(searchText: String, ownerId: String): List<Car> {
        return carRepository.searchCarsByOwner(searchText, ownerId)
    }
}
