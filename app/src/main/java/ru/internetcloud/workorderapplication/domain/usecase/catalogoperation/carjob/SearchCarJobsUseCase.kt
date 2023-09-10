package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.carjob

import ru.internetcloud.workorderapplication.domain.model.catalog.CarJob
import ru.internetcloud.workorderapplication.domain.repository.CarJobRepository
import javax.inject.Inject

class SearchCarJobsUseCase @Inject constructor(
    private val carJobRepository: CarJobRepository
) {
    suspend fun searchCarJobs(searchText: String): List<CarJob> {
        return carJobRepository.searchCarJobs(searchText)
    }
}
