package ru.internetcloud.workorderapplication.common.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class RepairTypeWithDetails(
    @Embedded
    val repairType: RepairTypeDbModel,

    @Relation(
        parentColumn = "id",
        entityColumn = "repairTypeId",
        entity = DefaultRepairTypeJobDetailDbModel::class
    )
    val jobDetails: List<DefaultRepairTypeJobDetailWithRequisities>
)
