package ru.internetcloud.workorderapplication.data.mapper

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
}
