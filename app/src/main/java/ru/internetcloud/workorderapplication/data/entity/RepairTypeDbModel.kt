package ru.internetcloud.workorderapplication.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repair_types")
data class RepairTypeDbModel(
    @PrimaryKey
    val id: String,
    val name: String
)
