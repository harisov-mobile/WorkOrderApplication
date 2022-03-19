package ru.internetcloud.workorderapplication.data.mapper

import ru.internetcloud.workorderapplication.data.entity.RepairTypeDbModel
import ru.internetcloud.workorderapplication.data.network.dto.RepairTypeDTO
import ru.internetcloud.workorderapplication.domain.catalog.RepairType
import javax.inject.Inject

class RepairTypeMapper @Inject constructor() {

    fun fromDtoToEntity(repairTypeDTO: RepairTypeDTO): RepairType {
        return RepairType(
            id = repairTypeDTO.id,
            name = repairTypeDTO.name
        )
    }

    fun fromListDtoToListEntity(list: List<RepairTypeDTO>) = list.map {
        fromDtoToEntity(it)
    }

    fun fromDbModelToEntityWithNull(repairTypeDbModel: RepairTypeDbModel?): RepairType? {
        var result: RepairType? = null
        if (repairTypeDbModel != null) {
            result = RepairType(
                id = repairTypeDbModel.id,
                name = repairTypeDbModel.name
            )
        }
        return result
    }

    fun fromDbModelToEntity(repairTypeDbModel: RepairTypeDbModel): RepairType {
            return RepairType(
                id = repairTypeDbModel.id,
                name = repairTypeDbModel.name
            )
    }

    fun fromListDbModelToListEntity(list: List<RepairTypeDbModel>) = list.map {
        fromDbModelToEntity(it)
    }

    fun fromEntityToDbModel(repairType: RepairType): RepairTypeDbModel {
        return RepairTypeDbModel(
            id = repairType.id,
            name = repairType.name
        )
    }

    fun fromListEntityToListDbModel(list: List<RepairType>) = list.map {
        fromEntityToDbModel(it)
    }

    fun fromListDtoToListDboModel(list: List<RepairTypeDTO>) = list.map {
        fromDtoToDbModel(it)
    }

    fun fromDtoToDbModel(repairTypeDTO: RepairTypeDTO): RepairTypeDbModel {
        return RepairTypeDbModel(
            id = repairTypeDTO.id,
            name = repairTypeDTO.name
        )
    }
}
