package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.carjob

import ru.internetcloud.workorderapplication.di.qualifiers.repository.DbCarJobRepositoryQualifier
import ru.internetcloud.workorderapplication.domain.repository.CarJobRepository
import javax.inject.Inject

class DeleteCarJobsUseCase @Inject constructor(@DbCarJobRepositoryQualifier private val carJobRepository: CarJobRepository) {
    suspend fun deleteAllCarJobs() {
        return carJobRepository.deleteAllCarJobs()
    }
}
