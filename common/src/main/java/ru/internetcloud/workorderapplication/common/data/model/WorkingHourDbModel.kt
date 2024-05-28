package ru.internetcloud.workorderapplication.common.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "working_hours")
data class WorkingHourDbModel(
    @PrimaryKey
    val id: String,
    val code1C: String,
    val name: String,
    val price: BigDecimal
)
