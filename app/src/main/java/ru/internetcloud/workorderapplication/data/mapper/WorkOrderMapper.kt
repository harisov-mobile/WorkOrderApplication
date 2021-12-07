package ru.internetcloud.workorderapplication.data.mapper

import ru.internetcloud.workorderapplication.data.entity.WorkOrderDbModel
import ru.internetcloud.workorderapplication.domain.document.WorkOrder

class WorkOrderMapper {

    fun fromEntityToDbModel(workOrder: WorkOrder): WorkOrderDbModel {
        return WorkOrderDbModel(
            id = workOrder.id,
            id1C = workOrder.id1C,
            number = workOrder.number,
            date = workOrder.date,
            mileage = workOrder.mileage
        )
    }

    fun fromDbModelToEntity(workOrderDbModel: WorkOrderDbModel): WorkOrder {
        return WorkOrder(
            id = workOrderDbModel.id,
            id1C = workOrderDbModel.id1C,
            number = workOrderDbModel.number,
            date = workOrderDbModel.date,
            mileage = workOrderDbModel.mileage
        )
    }
}
