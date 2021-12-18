package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.workinghour

import ru.internetcloud.workorderapplication.domain.catalog.CarJob
import ru.internetcloud.workorderapplication.domain.repository.CarJobRepository

class GetWorkingHourUseCase(private val carJobRepository: CarJobRepository) {
    suspend fun getCarJob(id: String): CarJob? {
        return carJobRepository.getCarJob(id)
    }
}
