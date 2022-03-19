package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.carmodel

import ru.internetcloud.workorderapplication.di.qualifiers.repository.DbCarModelRepositoryQualifier
import ru.internetcloud.workorderapplication.domain.catalog.CarModel
import ru.internetcloud.workorderapplication.domain.repository.CarModelRepository
import javax.inject.Inject

class AddCarModelListUseCase @Inject constructor(
    @DbCarModelRepositoryQualifier private val carModelRepository: CarModelRepository
) {

    suspend fun addCarModelList(carModelList: List<CarModel>) {
        return carModelRepository.addCarModelList(carModelList)
    }
}
