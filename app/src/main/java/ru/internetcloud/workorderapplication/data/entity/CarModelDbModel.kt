package ru.internetcloud.workorderapplication.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "car_models")
data class CarModelDbModel(
    @PrimaryKey
    val id: String,
    val code1C: String,
    val name: String
)
