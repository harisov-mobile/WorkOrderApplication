package ru.internetcloud.workorderapplication.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "performer_details")
data class PerformerDetailDbModel(
    @PrimaryKey
    val id: String = "",
    val lineNumber: Int = 0,
    val employeeId: String = "",
    val workOrderId: String = ""
)
