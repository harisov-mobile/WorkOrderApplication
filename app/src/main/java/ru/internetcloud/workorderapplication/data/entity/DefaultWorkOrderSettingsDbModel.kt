package ru.internetcloud.workorderapplication.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "default_work_order_settings")
data class DefaultWorkOrderSettingsDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val departmentId: String,
    val masterId: String
)
