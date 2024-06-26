package ru.internetcloud.workorderapplication.common.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cars")
data class CarDbModel(
    @PrimaryKey
    val id: String,
    val code1C: String,
    val name: String,
    var vin: String,
    var manufacturer: String, // производитель
    var model: String = "", // старое поле
    var carModelId: String = "", // id модели
    var type: String, // папка в спр-ке "Модели"
    var releaseYear: Int = 0, // год выпуска
    var mileage: Int, // пробег
    var ownerId: String = "" // id владельца
)
