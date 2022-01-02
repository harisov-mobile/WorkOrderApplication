package ru.internetcloud.workorderapplication.data.mapper

import ru.internetcloud.workorderapplication.data.entity.CarDbModel
import ru.internetcloud.workorderapplication.data.entity.CarWithOwner
import ru.internetcloud.workorderapplication.data.network.dto.CarDTO
import ru.internetcloud.workorderapplication.domain.catalog.Car
import ru.internetcloud.workorderapplication.domain.catalog.Partner

class CarMapper {

    private val partnerMapper = PartnerMapper()

    fun fromDtoToEntity(carDTO: CarDTO): Car {
        return Car(
            id = carDTO.id,
            code1C = carDTO.code1C,
            name = carDTO.name,
            vin = carDTO.vin,
            manufacturer = carDTO.manufacturer,
            model = carDTO.model,
            type = carDTO.type,
            releaseYear = carDTO.releaseYear,
            mileage = carDTO.mileage,
            owner = Partner(carDTO.ownerId)
        )
    }

    fun fromDbModelToEntityWithNull(carWithOwner: CarWithOwner?): Car? {
        var result: Car? = null
        if (carWithOwner != null) {

            return Car(
                id = carWithOwner.car.id,
                code1C = carWithOwner.car.code1C,
                name = carWithOwner.car.name,
                vin = carWithOwner.car.vin,
                manufacturer = carWithOwner.car.manufacturer,
                model = carWithOwner.car.model,
                type = carWithOwner.car.type,
                releaseYear = carWithOwner.car.releaseYear,
                mileage = carWithOwner.car.mileage,
                owner = partnerMapper.fromDbModelToEntityWithNull(carWithOwner.owner)
            )
        }
        return result
    }

    fun fromDbModelToEntity(carWithOwner: CarWithOwner): Car {
        return Car(
            id = carWithOwner.car.id,
            code1C = carWithOwner.car.code1C,
            name = carWithOwner.car.name,
            vin = carWithOwner.car.vin,
            manufacturer = carWithOwner.car.manufacturer,
            model = carWithOwner.car.model,
            type = carWithOwner.car.type,
            releaseYear = carWithOwner.car.releaseYear,
            mileage = carWithOwner.car.mileage,
            owner = partnerMapper.fromDbModelToEntityWithNull(carWithOwner.owner)
        )
    }

    fun fromEntityToDbModel(car: Car): CarDbModel {
        return CarDbModel(
            id = car.id,
            code1C = car.code1C,
            name = car.name,
            vin = car.vin,
            manufacturer = car.manufacturer,
            model = car.model,
            type = car.type,
            releaseYear = car.releaseYear,
            mileage = car.mileage,
            ownerId = car.owner?.id ?: ""
        )
    }

    fun fromListDtoToListEntity(list: List<CarDTO>) = list.map {
        fromDtoToEntity(it)
    }

    fun fromListDbModelToListEntity(list: List<CarWithOwner>) = list.map {
        fromDbModelToEntity(it)
    }

    fun fromListEntityToListDbModel(list: List<Car>) = list.map {
        fromEntityToDbModel(it)
    }
}
