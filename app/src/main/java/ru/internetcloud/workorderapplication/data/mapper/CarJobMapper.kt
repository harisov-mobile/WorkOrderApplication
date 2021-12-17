package ru.internetcloud.workorderapplication.data.mapper

import ru.internetcloud.workorderapplication.data.entity.CarJobDbModel
import ru.internetcloud.workorderapplication.data.network.dto.CarJobDTO
import ru.internetcloud.workorderapplication.domain.catalog.CarJob

class CarJobMapper {

    fun fromDtoToEntity(carJobDTO: CarJobDTO): CarJob {
        return CarJob(
            id = carJobDTO.id,
            code1C = carJobDTO.code1C,
            name = carJobDTO.name
        )
    }

    fun fromDbModelToEntity(carJobDbModel: CarJobDbModel): CarJob {
        return CarJob(
            id = carJobDbModel.id,
            code1C = carJobDbModel.code1C,
            name = carJobDbModel.name
        )
    }

    fun fromEntityToDbModel(carJob: CarJob): CarJobDbModel {
        return CarJobDbModel(
            id = carJob.id,
            code1C = carJob.code1C,
            name = carJob.name
        )
    }

    fun fromListDtoToListEntity(list: List<CarJobDTO>) = list.map {
        fromDtoToEntity(it)
    }

    fun fromListDbModelToListEntity(list: List<CarJobDbModel>) = list.map {
        fromDbModelToEntity(it)
    }

    fun fromListEntityToListDbModel(list: List<CarJob>) = list.map {
        fromEntityToDbModel(it)
    }
}
