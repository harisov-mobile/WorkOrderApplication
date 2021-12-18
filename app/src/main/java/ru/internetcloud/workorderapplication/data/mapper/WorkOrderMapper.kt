package ru.internetcloud.workorderapplication.data.mapper

import ru.internetcloud.workorderapplication.data.entity.WorkOrderDbModel
import ru.internetcloud.workorderapplication.domain.document.WorkOrder

class WorkOrderMapper {

    fun fromEntityToDbModel(workOrder: WorkOrder): WorkOrderDbModel {
        return WorkOrderDbModel(
            id = workOrder.id,
            number = workOrder.number,
            date = workOrder.date,
            mileage = workOrder.mileage
        )
    }

    fun fromDbModelToEntity(workOrderDbModel: WorkOrderDbModel): WorkOrder {
        return WorkOrder(
            id = workOrderDbModel.id,
            number = workOrderDbModel.number,
            date = workOrderDbModel.date,
            mileage = workOrderDbModel.mileage
        )
    }

    fun fromListDbModelToListEntity(list: List<WorkOrderDbModel>) = list.map {
        fromDbModelToEntity(it)
    }
}
