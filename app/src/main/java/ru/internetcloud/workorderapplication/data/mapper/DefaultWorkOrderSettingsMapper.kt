package ru.internetcloud.workorderapplication.data.mapper

import javax.inject.Inject
import ru.internetcloud.workorderapplication.data.model.DefaultWorkOrderSettingsDbModel
import ru.internetcloud.workorderapplication.data.model.DefaultWorkOrderSettingsWithRequisities
import ru.internetcloud.workorderapplication.data.network.dto.DefaultWorkOrderSettingsDTO
import ru.internetcloud.workorderapplication.domain.document.DefaultWorkOrderSettings

class DefaultWorkOrderSettingsMapper @Inject constructor(
    private val departmentMapper: DepartmentMapper,
    private val employeeMapper: EmployeeMapper,
    private val workingHourMapper: WorkingHourMapper
) {

    fun fromDtoToDbModel(defDTO: DefaultWorkOrderSettingsDTO): DefaultWorkOrderSettingsDbModel {
        return DefaultWorkOrderSettingsDbModel(
            departmentId = defDTO.departmentId,
            employeeId = defDTO.employeeId,
            masterId = defDTO.masterId,
            workingHourId = defDTO.workingHourId,
            defaultTimeNorm = defDTO.defaultTimeNorm
        )
    }

    fun fromDbModelToEntityWithNull(defWithReq: DefaultWorkOrderSettingsWithRequisities?): DefaultWorkOrderSettings? {
        var result: DefaultWorkOrderSettings? = null
        defWithReq?.let {
            result = DefaultWorkOrderSettings(
                department = departmentMapper.fromDbModelToEntityWithNull(it.department),
                employee = employeeMapper.fromDbModelToEntityWithNull(it.employee),
                master = employeeMapper.fromDbModelToEntityWithNull(it.master),
                workingHour = workingHourMapper.fromDbModelToEntityWithNull(it.workingHour),
                defaultTimeNorm = it.defaultWorkOrderSettingsDbModel.defaultTimeNorm
            )
        }
        return result
    }

    fun fromListDtoToListDbModel(list: List<DefaultWorkOrderSettingsDTO>) = list.map {
        fromDtoToDbModel(it)
    }
}
