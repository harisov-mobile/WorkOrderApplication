package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.carjob

import ru.internetcloud.workorderapplication.domain.catalog.CarJob
import ru.internetcloud.workorderapplication.domain.repository.CarJobRepository

class SearchCarJobsUseCase(private val carJobRepository: CarJobRepository) {
    suspend fun searchCarJobs(searchText: String): List<CarJob> {
        return carJobRepository.searchCarJobs(searchText)
    }
}
