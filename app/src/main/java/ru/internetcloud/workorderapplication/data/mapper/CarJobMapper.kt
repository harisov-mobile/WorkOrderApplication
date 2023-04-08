package ru.internetcloud.workorderapplication.data.mapper

import ru.internetcloud.workorderapplication.data.model.CarJobDbModel
import ru.internetcloud.workorderapplication.data.network.dto.CarJobDTO
import ru.internetcloud.workorderapplication.domain.catalog.CarJob
import javax.inject.Inject

class CarJobMapper @Inject constructor() {

    fun fromDtoToEntity(carJobDTO: CarJobDTO): CarJob {
        return CarJob(
            id = carJobDTO.id,
            code1C = carJobDTO.code1C,
            name = carJobDTO.name,
            folder = carJobDTO.folder
        )
    }

    fun fromDbModelToEntity(carJobDbModel: CarJobDbModel): CarJob {
        return CarJob(
            id = carJobDbModel.id,
            code1C = carJobDbModel.code1C,
            name = carJobDbModel.name,
            folder = carJobDbModel.folder
        )
    }

    fun fromDbModelToEntityWithNull(carJobDbModel: CarJobDbModel?): CarJob? {
        var result: CarJob? = null
        carJobDbModel?.let {
            result = CarJob(
                id = carJobDbModel.id,
                code1C = carJobDbModel.code1C,
                name = carJobDbModel.name,
                folder = carJobDbModel.folder
            )
        }
        return result
    }

    fun fromEntityToDbModel(carJob: CarJob): CarJobDbModel {
        return CarJobDbModel(
            id = carJob.id,
            code1C = carJob.code1C,
            name = carJob.name,
            folder = carJob.folder
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
