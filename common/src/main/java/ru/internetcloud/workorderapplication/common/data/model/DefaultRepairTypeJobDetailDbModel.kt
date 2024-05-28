package ru.internetcloud.workorderapplication.common.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "default_repair_type_job_details")
data class DefaultRepairTypeJobDetailDbModel(
    @PrimaryKey
    val id: String = "",
    val lineNumber: Int = 0,
    val carModelId: String = "",
    val carJobId: String = "",
    val quantity: BigDecimal = BigDecimal.ZERO,
    val repairTypeId: String = "" // очень важное поле - через него идет связь ТЧ к владельцу
)
