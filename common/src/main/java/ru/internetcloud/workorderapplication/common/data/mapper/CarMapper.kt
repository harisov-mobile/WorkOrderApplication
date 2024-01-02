package ru.internetcloud.workorderapplication.common.data.mapper

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.data.model.CarDbModel
import ru.internetcloud.workorderapplication.common.data.model.CarWithOwner
import ru.internetcloud.workorderapplication.common.data.network.dto.CarDTO
import ru.internetcloud.workorderapplication.common.domain.model.catalog.Car
import ru.internetcloud.workorderapplication.common.domain.model.catalog.CarModel
import ru.internetcloud.workorderapplication.common.domain.model.catalog.Partner

class CarMapper @Inject constructor(
    private val partnerMapper: PartnerMapper,
    private val carModelMapper: CarModelMapper
) {

    fun fromDtoToEntity(carDTO: CarDTO): Car {
        return Car(
            id = carDTO.id,
            code1C = carDTO.code1C,
            name = carDTO.name,
            vin = carDTO.vin,
            manufacturer = carDTO.manufacturer,
            carModel = CarModel(carDTO.carModelId), // специально псевдо-объект CarModel создаю, с единственной целью:
            type = carDTO.type,
            releaseYear = carDTO.releaseYear,
            mileage = carDTO.mileage,
            owner = Partner(carDTO.ownerId) // специально псевдо-объект Partner создаю, с единственной целью:
            // чтобы он хранил ownerId, мне понадобится ownerId потом
        )
    }

    fun fromEntityToDbModel(car: Car): CarDbModel {
        return CarDbModel(
            id = car.id,
            code1C = car.code1C,
            name = car.name,
            vin = car.vin,
            manufacturer = car.manufacturer,
            carModelId = car.carModel?.id ?: "",
            type = car.type,
            releaseYear = car.releaseYear,
            mileage = car.mileage,
            ownerId = car.owner?.id ?: ""
        )
    }

    fun fromListDtoToListEntity(list: List<CarDTO>) = list.map {
        fromDtoToEntity(it)
    }

    fun fromListEntityToListDbModel(list: List<Car>) = list.map {
        fromEntityToDbModel(it)
    }

    fun fromCarWithOwnerToEntityWithNull(carWithOwner: CarWithOwner?): Car? {
        var result: Car? = null
        if (carWithOwner != null) {
            return Car(
                id = carWithOwner.car.id,
                code1C = carWithOwner.car.code1C,
                name = carWithOwner.car.name,
                vin = carWithOwner.car.vin,
                manufacturer = carWithOwner.car.manufacturer,
                carModel = carModelMapper.fromDbModelToEntityWithNull(carWithOwner.carModel),
                type = carWithOwner.car.type,
                releaseYear = carWithOwner.car.releaseYear,
                mileage = carWithOwner.car.mileage,
                owner = partnerMapper.fromDbModelToEntityWithNull(carWithOwner.owner)
            )
        }
        return result
    }

    fun fromCarWithOwnerToEntity(carWithOwner: CarWithOwner): Car {
        return Car(
            id = carWithOwner.car.id,
            code1C = carWithOwner.car.code1C,
            name = carWithOwner.car.name,
            vin = carWithOwner.car.vin,
            manufacturer = carWithOwner.car.manufacturer,
            carModel = carModelMapper.fromDbModelToEntityWithNull(carWithOwner.carModel),
            type = carWithOwner.car.type,
            releaseYear = carWithOwner.car.releaseYear,
            mileage = carWithOwner.car.mileage,
            owner = partnerMapper.fromDbModelToEntityWithNull(carWithOwner.owner)
        )
    }

    fun fromListCarWithOwnerToListEntity(list: List<CarWithOwner>) = list.map {
        fromCarWithOwnerToEntity(it)
    }
}
