package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.carjob

import ru.internetcloud.workorderapplication.domain.repository.CarJobRepository

class DeleteCarJobsUseCase(private val carJobRepository: CarJobRepository) {
    suspend fun deleteAllCarJobs() {
        return carJobRepository.deleteAllCarJobs()
    }
}
