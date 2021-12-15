package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.carjob

import ru.internetcloud.workorderapplication.domain.catalog.CarJob
import ru.internetcloud.workorderapplication.domain.repository.CarJobRepository

class AddCarJobUseCase(private val carJobRepository: CarJobRepository) {
    suspend fun addCarJob(carJob: CarJob) {
        return carJobRepository.addCarJob(carJob)
    }
}
