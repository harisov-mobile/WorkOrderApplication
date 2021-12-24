package ru.internetcloud.workorderapplication.data.mapper

import ru.internetcloud.workorderapplication.data.entity.CarDbModel
import ru.internetcloud.workorderapplication.data.network.dto.CarDTO
import ru.internetcloud.workorderapplication.domain.catalog.Car
import ru.internetcloud.workorderapplication.domain.catalog.Partner

class CarMapper {

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

    fun fromDbModelToEntity(carDbModel: CarDbModel?): Car? {
        var result: Car? = null
        if (carDbModel != null) {

            result = Car(
                id = carDbModel.id,
                code1C = carDbModel.code1C,
                name = carDbModel.name
            )
        }
        return result
    }

    fun fromDbModelToEntityWithoutNull(carDbModel: CarDbModel): Car {
        return Car(
            id = carDbModel.id,
            code1C = carDbModel.code1C,
            name = carDbModel.name
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

    fun fromListDbModelToListEntity(list: List<CarDbModel>) = list.map {
        fromDbModelToEntityWithoutNull(it)
    }

    fun fromListEntityToListDbModel(list: List<Car>) = list.map {
        fromEntityToDbModel(it)
    }
}
