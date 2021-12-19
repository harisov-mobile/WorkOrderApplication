package ru.internetcloud.workorderapplication.data.mapper

import ru.internetcloud.workorderapplication.data.entity.WorkOrderDbModel
import ru.internetcloud.workorderapplication.data.network.dto.WorkOrderDTO
import ru.internetcloud.workorderapplication.domain.common.DateConverter
import ru.internetcloud.workorderapplication.domain.document.WorkOrder
import java.util.*

class WorkOrderMapper {

    fun fromEntityToDbModel(workOrder: WorkOrder): WorkOrderDbModel {
        return WorkOrderDbModel(
            id = workOrder.id,
            number = workOrder.number,
            date = workOrder.date
        )
    }

    fun fromDbModelToEntity(workOrderDbModel: WorkOrderDbModel): WorkOrder {
        return WorkOrder(
            id = workOrderDbModel.id,
            number = workOrderDbModel.number,
            date = workOrderDbModel.date
        )
    }

    fun fromListDbModelToListEntity(list: List<WorkOrderDbModel>) = list.map {
        fromDbModelToEntity(it)
    }

    fun fromListDtoToListDboModel(list: List<WorkOrderDTO>) = list.map {
        fromDtoToDbModel(it)
    }

    fun fromDtoToDbModel(workOrderDTO: WorkOrderDTO): WorkOrderDbModel {
        return WorkOrderDbModel(
            id = workOrderDTO.id,
            number = workOrderDTO.number,
            //date = DateConverter.fromStringToDate(workOrderDTO.dateString),
            date = Date(),
            partnerId = workOrderDTO.partnerId,
            carId = workOrderDTO.carId,
            repairTypeId = workOrderDTO.repairTypeId,
            departmentId = workOrderDTO.departmentId,
            requestReason = workOrderDTO.requestReason,
            masterId = workOrderDTO.masterId,
            comment = workOrderDTO.comment
        )
    }
}
