package ru.internetcloud.workorderapplication.data.network.dto

import com.google.gson.annotations.SerializedName

data class DepartmentDTO(
    @SerializedName("id")
    val id: String,

    @SerializedName("code1C")
    val code1C: String,

    @SerializedName("name")
    val name: String
)