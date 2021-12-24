package ru.internetcloud.workorderapplication.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class WorkOrderWithDetails(
    @Embedded
    val workOrder: WorkOrderDbModel,
    @Relation(
        parentColumn = "partnerId",
        entityColumn = "id",
        entity = PartnerDbModel::class
    )
    val partner: PartnerDbModel?,
    @Relation(
        parentColumn = "carId",
        entityColumn = "id",
        entity = CarDbModel::class
    )
    val car: CarDbModel?,
    @Relation(
        parentColumn = "repairTypeId",
        entityColumn = "id",
        entity = RepairTypeDbModel::class
    )
    val repairType: RepairTypeDbModel?,
    @Relation(
        parentColumn = "departmentId",
        entityColumn = "id",
        entity = DepartmentDbModel::class
    )
    val department: DepartmentDbModel?,
    @Relation(
        parentColumn = "masterId",
        entityColumn = "id",
        entity = EmployeeDbModel::class
    )
    val master: EmployeeDbModel?,
    @Relation(
        parentColumn = "id",
        entityColumn = "workOrderId",
        entity = PerformerDetailDbModel::class
    )
    val performers: List<PerformerDetailWithRequisities>,
    @Relation(
        parentColumn = "id",
        entityColumn = "workOrderId",
        entity = JobDetailDbModel::class
    )
    val jobDetails: List<JobDetailWithRequisities>
)
