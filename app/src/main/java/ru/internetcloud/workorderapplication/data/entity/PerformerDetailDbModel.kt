package ru.internetcloud.workorderapplication.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "performer_details")
data class PerformerDetailDbModel(
    @PrimaryKey
    var id: String = "",
    val employeeId: String = ""
)
