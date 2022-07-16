package ru.internetcloud.workorderapplication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "departments")
data class DepartmentDbModel(
    @PrimaryKey
    val id: String,
    val code1C: String,
    val name: String
)
