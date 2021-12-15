package ru.internetcloud.workorderapplication.data.mapper

import ru.internetcloud.workorderapplication.data.entity.RepairTypeDbModel
import ru.internetcloud.workorderapplication.data.network.dto.RepairTypeDTO
import ru.internetcloud.workorderapplication.domain.catalog.RepairType

class RepairTypeMapper {

    fun fromDtoToEntity(repairTypeDTO: RepairTypeDTO): RepairType {
        return RepairType(
            id1C = repairTypeDTO.id1C,
            name = repairTypeDTO.name
        )
    }

    fun fromListDtoToListEntity(list: List<RepairTypeDTO>) = list.map {
        fromDtoToEntity(it)
    }

    fun fromDbModelToEntity(repairTypeDbModel: RepairTypeDbModel): RepairType {
        return RepairType(
            id1C = repairTypeDbModel.id1C,
            name = repairTypeDbModel.name
        )
    }

    fun fromListDbModelToListEntity(list: List<RepairTypeDbModel>) = list.map {
        fromDbModelToEntity(it)
    }

    fun fromEntityToDbModel(repairType: RepairType): RepairTypeDbModel {
        return RepairTypeDbModel(
            id1C = repairType.id1C,
            name = repairType.name
        )
    }
}
