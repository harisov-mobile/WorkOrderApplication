package ru.internetcloud.workorderapplication.data.mapper

import ru.internetcloud.workorderapplication.data.entity.DefaultRepairTypeJobDetailDbModel
import ru.internetcloud.workorderapplication.data.network.dto.DefaultRepairTypeJobDetailDTO
import javax.inject.Inject

class DefaultRepairTypeJobDetailMapper @Inject constructor() {

    fun fromDtoToDbModel(defaultJobDetailDTO: DefaultRepairTypeJobDetailDTO): DefaultRepairTypeJobDetailDbModel {
        return DefaultRepairTypeJobDetailDbModel(
            id = defaultJobDetailDTO.id,
            lineNumber = defaultJobDetailDTO.lineNumber,
            carModelId = defaultJobDetailDTO.carModelId,
            carJobId = defaultJobDetailDTO.carJobId,
            quantity = defaultJobDetailDTO.quantity,
            repairTypeId = defaultJobDetailDTO.repairTypeId
        )
    }
}
