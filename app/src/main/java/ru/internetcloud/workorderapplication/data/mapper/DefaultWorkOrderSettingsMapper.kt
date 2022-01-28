package ru.internetcloud.workorderapplication.data.mapper

import ru.internetcloud.workorderapplication.data.entity.DefaultWorkOrderSettingsDbModel
import ru.internetcloud.workorderapplication.data.network.dto.DefaultWorkOrderSettingsDTO

class DefaultWorkOrderSettingsMapper {

    fun fromDtoToDbModel(defDTO: DefaultWorkOrderSettingsDTO): DefaultWorkOrderSettingsDbModel {
        return DefaultWorkOrderSettingsDbModel(
            departmentId = defDTO.departmentId,
            masterId = defDTO.masterId
        )
    }
}
