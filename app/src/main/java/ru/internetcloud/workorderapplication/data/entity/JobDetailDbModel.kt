package ru.internetcloud.workorderapplication.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "job_details")
data class JobDetailDbModel(
    @PrimaryKey
    var id: String = "",
    val carJobId: String = "",
    var quantity: BigDecimal = BigDecimal.ZERO,
    var timeNorm: BigDecimal = BigDecimal.ZERO,
    var workingHourId: String = "", // нормо-часы
    var sum: BigDecimal = BigDecimal.ZERO
)
