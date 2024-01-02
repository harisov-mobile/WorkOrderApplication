package ru.internetcloud.workorderapplication.common.data.model

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
