package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.car

import ru.internetcloud.workorderapplication.di.qualifiers.repository.DbCarRepositoryQualifier
import ru.internetcloud.workorderapplication.domain.repository.CarRepository
import javax.inject.Inject

class DeleteCarListUseCase @Inject constructor(@DbCarRepositoryQualifier private val carRepository: CarRepository) {

    suspend fun deleteAllCars() {
        return carRepository.deleteAllCars()
    }
}
