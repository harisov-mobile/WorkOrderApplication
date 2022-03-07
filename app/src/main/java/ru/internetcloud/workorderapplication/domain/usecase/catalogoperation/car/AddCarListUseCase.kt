package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.car

import ru.internetcloud.workorderapplication.di.qualifiers.repository.DbCarRepositoryQualifier
import ru.internetcloud.workorderapplication.domain.catalog.Car
import ru.internetcloud.workorderapplication.domain.repository.CarRepository
import javax.inject.Inject

class AddCarListUseCase @Inject constructor(
    @DbCarRepositoryQualifier private val carRepository: CarRepository
) {

    suspend fun addCarList(carList: List<Car>) {
        return carRepository.addCarList(carList)
    }
}
