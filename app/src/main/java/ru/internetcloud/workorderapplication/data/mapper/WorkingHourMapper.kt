package ru.internetcloud.workorderapplication.data.mapper

import javax.inject.Inject
import ru.internetcloud.workorderapplication.data.model.WorkingHourDbModel
import ru.internetcloud.workorderapplication.data.network.dto.WorkingHourDTO
import ru.internetcloud.workorderapplication.domain.catalog.WorkingHour

class WorkingHourMapper @Inject constructor() {

    fun fromDtoToEntity(workingHourDTO: WorkingHourDTO): WorkingHour {
        return WorkingHour(
            id = workingHourDTO.id,
            code1C = workingHourDTO.code1C,
            name = workingHourDTO.name,
            price = workingHourDTO.price
        )
    }

    fun fromDbModelToEntity(workingHourDbModel: WorkingHourDbModel): WorkingHour {
        return WorkingHour(
            id = workingHourDbModel.id,
            code1C = workingHourDbModel.code1C,
            name = workingHourDbModel.name,
            price = workingHourDbModel.price
        )
    }

    fun fromDbModelToEntityWithNull(workingHourDbModel: WorkingHourDbModel?): WorkingHour? {
        var result: WorkingHour? = null
        workingHourDbModel?.let {
            result = WorkingHour(
                id = workingHourDbModel.id,
                code1C = workingHourDbModel.code1C,
                name = workingHourDbModel.name,
                price = workingHourDbModel.price
            )
        }
        return result
    }

    fun fromEntityToDbModel(workingHour: WorkingHour): WorkingHourDbModel {
        return WorkingHourDbModel(
            id = workingHour.id,
            code1C = workingHour.code1C,
            name = workingHour.name,
            price = workingHour.price
        )
    }

    fun fromListDtoToListEntity(list: List<WorkingHourDTO>) = list.map {
        fromDtoToEntity(it)
    }

    fun fromListDbModelToListEntity(list: List<WorkingHourDbModel>) = list.map {
        fromDbModelToEntity(it)
    }

    fun fromListEntityToListDbModel(list: List<WorkingHour>) = list.map {
        fromEntityToDbModel(it)
    }
}
