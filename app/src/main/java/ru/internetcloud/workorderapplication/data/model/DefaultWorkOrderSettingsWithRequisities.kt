package ru.internetcloud.workorderapplication.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class DefaultWorkOrderSettingsWithRequisities(
    @Embedded
    val defaultWorkOrderSettingsDbModel: DefaultWorkOrderSettingsDbModel,

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
        parentColumn = "employeeId",
        entityColumn = "id",
        entity = EmployeeDbModel::class
    )
    val employee: EmployeeDbModel?,

    @Relation(
        parentColumn = "workingHourId",
        entityColumn = "id",
        entity = WorkingHourDbModel::class
    )
    val workingHour: WorkingHourDbModel?
)
