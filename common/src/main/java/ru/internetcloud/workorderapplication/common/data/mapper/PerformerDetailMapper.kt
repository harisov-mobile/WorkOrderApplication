package ru.internetcloud.workorderapplication.common.data.mapper

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.data.model.PerformerDetailDbModel
import ru.internetcloud.workorderapplication.common.data.model.PerformerDetailWithRequisities
import ru.internetcloud.workorderapplication.common.data.network.dto.PerformerDetailDTO
import ru.internetcloud.workorderapplication.common.domain.model.document.PerformerDetail

class PerformerDetailMapper @Inject constructor(
    private val employeeMapper: EmployeeMapper
) {

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

    fun fromListDtoToDbModel(list: List<PerformerDetailDTO>): List<PerformerDetailDbModel> {
        return list.map { fromDtoToDbModel(it) }
    }
}
