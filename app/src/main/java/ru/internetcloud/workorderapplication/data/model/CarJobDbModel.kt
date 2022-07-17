package ru.internetcloud.workorderapplication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "car_jobs")
data class CarJobDbModel(
    @PrimaryKey
    val id: String,
    val code1C: String,
    val name: String,
    val folder: String

)
