package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.carjob

import ru.internetcloud.workorderapplication.di.qualifiers.repository.DbCarJobRepositoryQualifier
import ru.internetcloud.workorderapplication.domain.catalog.CarJob
import ru.internetcloud.workorderapplication.domain.repository.CarJobRepository
import javax.inject.Inject

class AddCarJobListUseCase @Inject constructor(
    @DbCarJobRepositoryQualifier private val carJobRepository: CarJobRepository
) {

    suspend fun addCarJobList(carJobList: List<CarJob>) {
        return carJobRepository.addCarJobList(carJobList)
    }
}
