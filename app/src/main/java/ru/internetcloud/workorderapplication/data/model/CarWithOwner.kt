package ru.internetcloud.workorderapplication.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class CarWithOwner(
    @Embedded
    val car: CarDbModel,

    @Relation(
        parentColumn = "ownerId",
        entityColumn = "id",
        entity = PartnerDbModel::class
    )
    val owner: PartnerDbModel?,

    @Relation(
        parentColumn = "carModelId",
        entityColumn = "id",
        entity = CarModelDbModel::class
    )
    val carModel: CarModelDbModel?
)
