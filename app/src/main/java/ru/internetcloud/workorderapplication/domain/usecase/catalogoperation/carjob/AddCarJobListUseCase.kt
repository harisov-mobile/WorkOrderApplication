package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.carjob

import ru.internetcloud.workorderapplication.domain.catalog.CarJob
import ru.internetcloud.workorderapplication.domain.repository.CarJobRepository

class AddCarJobListUseCase(private val carJobRepository: CarJobRepository) {
    suspend fun addCarJobList(carJobList: List<CarJob>) {
        return carJobRepository.addCarJobList(carJobList)
    }
}
