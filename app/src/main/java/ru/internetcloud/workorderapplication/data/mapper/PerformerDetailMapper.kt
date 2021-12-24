package ru.internetcloud.workorderapplication.data.mapper

import ru.internetcloud.workorderapplication.data.entity.JobDetailWithRequisities
import ru.internetcloud.workorderapplication.data.entity.PerformerDetailDbModel
import ru.internetcloud.workorderapplication.data.entity.PerformerDetailWithRequisities
import ru.internetcloud.workorderapplication.data.entity.WorkOrderWithDetails
import ru.internetcloud.workorderapplication.data.network.dto.PerformerDetailDTO
import ru.internetcloud.workorderapplication.domain.catalog.Employee
import ru.internetcloud.workorderapplication.domain.document.JobDetail
import ru.internetcloud.workorderapplication.domain.document.PerformerDetail
import ru.internetcloud.workorderapplication.domain.document.WorkOrder

class PerformerDetailMapper {

    private val employeeMapper = EmployeeMapper()

    fun fromDtoToDbModel(performerDetailDTO: PerformerDetailDTO): PerformerDetailDbModel {
        return PerformerDetailDbModel(
            id = performerDetailDTO.id,
            employeeId = performerDetailDTO.employeeId,
            workOrderId = performerDetailDTO.workOrderId
        )
    }

    fun fromListDbModelToListEntity(list: List<PerformerDetailDbModel>) = list.map {
        fromDbModelToEntity(it)
    }

    fun fromDbModelToEntity(performerDetailDbModel: PerformerDetailDbModel): PerformerDetail {
        return PerformerDetail(
            id = performerDetailDbModel.id
        )
    }

    fun fromListDbToListEntity(list: List<PerformerDetailWithRequisities>) = list.map {
        fromDbToEntity(it)
    }

    fun fromDbToEntity(performerDetailWithRequisities: PerformerDetailWithRequisities): PerformerDetail {
        return PerformerDetail(
            id = performerDetailWithRequisities.performerDetailDbModel.id,
            employee = employeeMapper.fromDbModelToEntityWithNull(performerDetailWithRequisities.employee)
        )
    }
}
