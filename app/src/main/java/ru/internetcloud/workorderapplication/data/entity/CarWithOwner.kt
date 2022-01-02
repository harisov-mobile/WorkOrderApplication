package ru.internetcloud.workorderapplication.data.entity

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
    val owner: PartnerDbModel?
)
