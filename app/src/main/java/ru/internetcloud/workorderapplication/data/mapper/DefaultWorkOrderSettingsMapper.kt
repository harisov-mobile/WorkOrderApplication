package ru.internetcloud.workorderapplication.data.mapper

import ru.internetcloud.workorderapplication.data.entity.DefaultWorkOrderSettingsDbModel
import ru.internetcloud.workorderapplication.data.entity.DefaultWorkOrderSettingsWithRequisities
import ru.internetcloud.workorderapplication.data.network.dto.DefaultWorkOrderSettingsDTO
import ru.internetcloud.workorderapplication.domain.document.DefaultWorkOrderSettings
import javax.inject.Inject

class DefaultWorkOrderSettingsMapper @Inject constructor(
    private val departmentMapper: DepartmentMapper,
    private val employeeMapper: EmployeeMapper
) {

    fun fromDtoToDbModel(defDTO: DefaultWorkOrderSettingsDTO): DefaultWorkOrderSettingsDbModel {
        return DefaultWorkOrderSettingsDbModel(
            departmentId = defDTO.departmentId,
            employeeId = defDTO.employeeId,
            masterId = defDTO.masterId
        )
    }

    fun fromDbModelToEntityWithNull(defWithReq: DefaultWorkOrderSettingsWithRequisities?): DefaultWorkOrderSettings? {
        var result: DefaultWorkOrderSettings? = null
        defWithReq?.let {
            result = DefaultWorkOrderSettings(
                department = departmentMapper.fromDbModelToEntityWithNull(it.department),
                employee = employeeMapper.fromDbModelToEntityWithNull(it.employee),
                master = employeeMapper.fromDbModelToEntityWithNull(it.master)
            )
        }
        return result
    }
}
