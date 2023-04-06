package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.carjob

import ru.internetcloud.workorderapplication.domain.catalog.CarJob
import ru.internetcloud.workorderapplication.domain.repository.CarJobRepository
import javax.inject.Inject

class GetCarJobListUseCase @Inject constructor(
    private val carJobRepository: CarJobRepository
) {
    suspend fun getCarJobList(): List<CarJob> {
        return carJobRepository.getCarJobList()
    }
}
