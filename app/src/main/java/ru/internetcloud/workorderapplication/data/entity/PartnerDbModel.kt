package ru.internetcloud.workorderapplication.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "partners")
data class PartnerDbModel(
    @PrimaryKey
    val id: String,
    val code1C: String,
    val name: String,
    var fullName: String = "",
    var inn: String = "",
    var kpp: String = ""
)
