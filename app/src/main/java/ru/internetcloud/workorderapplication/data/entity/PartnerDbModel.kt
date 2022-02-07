package ru.internetcloud.workorderapplication.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "partners")
data class PartnerDbModel(
    @PrimaryKey
    val id: String,
    val code1C: String,
    val name: String,
    val fullName: String = "",
    val inn: String = "",
    val kpp: String = ""
)
