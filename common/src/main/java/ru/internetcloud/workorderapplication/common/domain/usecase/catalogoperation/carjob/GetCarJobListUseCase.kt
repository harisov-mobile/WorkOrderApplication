package ru.internetcloud.workorderapplication.common.domain.usecase.catalogoperation.carjob

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.domain.model.catalog.CarJob
import ru.internetcloud.workorderapplication.common.domain.repository.CarJobRepository

class GetCarJobListUseCase @Inject constructor(
    private val carJobRepository: CarJobRepository
) {
    suspend fun getCarJobList(): List<CarJob> {
        return carJobRepository.getCarJobList()
    }
}
