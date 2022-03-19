package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.carmodel

import ru.internetcloud.workorderapplication.di.qualifiers.repository.DbCarModelRepositoryQualifier
import ru.internetcloud.workorderapplication.domain.repository.CarModelRepository
import javax.inject.Inject

class DeleteCarModelsUseCase @Inject constructor(
    @DbCarModelRepositoryQualifier private val carModelRepository: CarModelRepository) {

    suspend fun deleteAllCarModels() {
        return carModelRepository.deleteAllCarModels()
    }
}