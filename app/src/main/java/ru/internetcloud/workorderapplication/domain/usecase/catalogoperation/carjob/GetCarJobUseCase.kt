package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.carjob

import ru.internetcloud.workorderapplication.domain.catalog.CarJob
import ru.internetcloud.workorderapplication.domain.repository.CarJobRepository

class GetCarJobUseCase(private val carJobRepository: CarJobRepository) {
    suspend fun getCarJob(id: String): CarJob? {
        return carJobRepository.getCarJob(id)
    }
}
