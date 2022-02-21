package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.carjob

import ru.internetcloud.workorderapplication.domain.repository.CarJobRepository
import javax.inject.Inject

class DeleteCarJobsUseCase @Inject constructor(private val carJobRepository: CarJobRepository) {
    suspend fun deleteAllCarJobs() {
        return carJobRepository.deleteAllCarJobs()
    }
}
