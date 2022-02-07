package ru.internetcloud.workorderapplication.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class PerformerDetailWithRequisities(
    @Embedded
    val performerDetailDbModel: PerformerDetailDbModel,
    @Relation(
        parentColumn = "employeeId",
        entityColumn = "id",
        entity = EmployeeDbModel::class
    )
    val employee: EmployeeDbModel?
)
