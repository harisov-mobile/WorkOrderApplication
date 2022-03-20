package ru.internetcloud.workorderapplication.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "default_work_order_settings")
data class DefaultWorkOrderSettingsDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val departmentId: String,
    val employeeId: String,
    val masterId: String,
    val workingHourId: String,
    var defaultTimeNorm: BigDecimal
)
