package ru.internetcloud.workorderapplication.common.domain.usecase.catalogoperation.car

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.domain.model.catalog.Car
import ru.internetcloud.workorderapplication.common.domain.repository.CarRepository

class SearchCarsByOwnerUseCase @Inject constructor(
    private val carRepository: CarRepository
) {
    suspend fun searchCarsByOwner(searchText: String, ownerId: String): List<Car> {
        return carRepository.searchCarsByOwner(searchText, ownerId)
    }
}
