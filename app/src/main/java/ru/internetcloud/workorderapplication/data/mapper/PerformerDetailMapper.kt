package ru.internetcloud.workorderapplication.data.mapper

import ru.internetcloud.workorderapplication.data.entity.PerformerDetailDbModel
import ru.internetcloud.workorderapplication.data.entity.PerformerDetailWithRequisities
import ru.internetcloud.workorderapplication.data.network.dto.PerformerDetailDTO
import ru.internetcloud.workorderapplication.domain.document.PerformerDetail

class PerformerDetailMapper {

    private val employeeMapper = EmployeeMapper()

    fun fromDtoToDbModel(performerDetailDTO: PerformerDetailDTO): PerformerDetailDbModel {
        return PerformerDetailDbModel(
            id = performerDetailDTO.id,
            lineNumber = performerDetailDTO.lineNumber,
            employeeId = performerDetailDTO.employeeId,
            workOrderId = performerDetailDTO.workOrderId
        )
    }

    fun fromListDbModelToListEntity(list: List<PerformerDetailDbModel>) = list.map {
        fromDbModelToEntity(it)
    }

    fun fromDbModelToEntity(performerDetailDbModel: PerformerDetailDbModel): PerformerDetail {
        return PerformerDetail(
            id = performerDetailDbModel.id,
            lineNumber = performerDetailDbModel.lineNumber
        )
    }

    fun fromListDbToListEntity(list: List<PerformerDetailWithRequisities>): MutableList<PerformerDetail> {
        val sortedList = list.sortedBy {
            it.performerDetailDbModel.lineNumber
        }

        val result: MutableList<PerformerDetail> = mutableListOf()

        sortedList.forEach {
            result.add(fromDbToEntity(it))
        }

        return result
    }

    fun fromDbToEntity(performerDetailWithRequisities: PerformerDetailWithRequisities): PerformerDetail {
        return PerformerDetail(
            id = performerDetailWithRequisities.performerDetailDbModel.id,
            lineNumber = performerDetailWithRequisities.performerDetailDbModel.lineNumber,
            employee = employeeMapper.fromDbModelToEntityWithNull(performerDetailWithRequisities.employee)
        )
    }

    fun fromListEntityToListDbModel(list: List<PerformerDetail>, workOrderId: String) = list.map {
        fromEntityToDbModel(it, workOrderId)
    }

    fun fromEntityToDbModel(performerDetail: PerformerDetail, workOrderId: String): PerformerDetailDbModel {
        return PerformerDetailDbModel(
            id = performerDetail.id,
            lineNumber = performerDetail.lineNumber,
            employeeId = performerDetail.employee?.id ?: "",
            workOrderId = workOrderId
        )
    }
}
