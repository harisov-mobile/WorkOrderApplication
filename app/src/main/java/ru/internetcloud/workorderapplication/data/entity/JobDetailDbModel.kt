package ru.internetcloud.workorderapplication.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "job_details")
data class JobDetailDbModel(
    @PrimaryKey
    val id: String = "",
    var lineNumber: Int = 0,
    val carJobId: String = "",
    val quantity: BigDecimal = BigDecimal.ZERO,
    val timeNorm: BigDecimal = BigDecimal.ZERO,
    val workingHourId: String = "", // нормо-часы
    val sum: BigDecimal = BigDecimal.ZERO,
    val workOrderId: String = ""
)
