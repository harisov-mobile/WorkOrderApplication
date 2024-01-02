package ru.internetcloud.workorderapplication.common.data.mapper

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.data.model.CarModelDbModel
import ru.internetcloud.workorderapplication.common.data.network.dto.CarModelDTO
import ru.internetcloud.workorderapplication.common.domain.model.catalog.CarModel

class CarModelMapper @Inject constructor() {

    fun fromDtoToEntity(carModelDTO: CarModelDTO): CarModel {
        return CarModel(
            id = carModelDTO.id,
            code1C = carModelDTO.code1C,
            name = carModelDTO.name
        )
    }

    fun fromDbModelToEntityWithNull(carModelDbModel: CarModelDbModel?): CarModel? {
        var result: CarModel? = null
        if (carModelDbModel != null) {
            result = CarModel(
                id = carModelDbModel.id,
                code1C = carModelDbModel.code1C,
                name = carModelDbModel.name
            )
        }
        return result
    }

    fun fromDbModelToEntity(carModelDbModel: CarModelDbModel): CarModel {
        return CarModel(
            id = carModelDbModel.id,
            code1C = carModelDbModel.code1C,
            name = carModelDbModel.name
        )
    }

    fun fromEntityToDbModel(partner: CarModel): CarModelDbModel {
        return CarModelDbModel(
            id = partner.id,
            code1C = partner.code1C,
            name = partner.name
        )
    }

    fun fromListDtoToListEntity(list: List<CarModelDTO>) = list.map {
        fromDtoToEntity(it)
    }

    fun fromListDbModelToListEntity(list: List<CarModelDbModel>) = list.map {
        fromDbModelToEntity(it)
    }

    fun fromListEntityToListDbModel(list: List<CarModel>) = list.map {
        fromEntityToDbModel(it)
    }
}
