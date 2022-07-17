package ru.internetcloud.workorderapplication.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class JobDetailWithRequisities(
    @Embedded
    val jobDetailDbModel: JobDetailDbModel,
    @Relation(
        parentColumn = "carJobId",
        entityColumn = "id",
        entity = CarJobDbModel::class
    )
    val carJob: CarJobDbModel?,
    @Relation(
        parentColumn = "workingHourId",
        entityColumn = "id",
        entity = WorkingHourDbModel::class
    )
    val workingHour: WorkingHourDbModel?
)
