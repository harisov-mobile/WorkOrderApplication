package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.carjob

import javax.inject.Inject
import ru.internetcloud.workorderapplication.domain.catalog.CarJob
import ru.internetcloud.workorderapplication.domain.repository.CarJobRepository

class GetCarJobListUseCase @Inject constructor(
    private val carJobRepository: CarJobRepository
) {
    suspend fun getCarJobList(): List<CarJob> {
        return carJobRepository.getCarJobList()
    }
}
