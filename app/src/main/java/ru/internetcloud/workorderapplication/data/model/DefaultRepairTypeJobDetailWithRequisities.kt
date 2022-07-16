package ru.internetcloud.workorderapplication.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class DefaultRepairTypeJobDetailWithRequisities(
    @Embedded
    val defaultRepairTypeJobDetailDbModel: DefaultRepairTypeJobDetailDbModel,

    @Relation(
        parentColumn = "carModelId",
        entityColumn = "id",
        entity = CarModelDbModel::class
    )
    val carModel: CarModelDbModel?,

    @Relation(
        parentColumn = "carJobId",
        entityColumn = "id",
        entity = CarJobDbModel::class
    )
    val carJob: CarJobDbModel?
)
