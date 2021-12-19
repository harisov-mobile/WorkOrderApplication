package ru.internetcloud.workorderapplication.data.network.dto

import com.google.gson.annotations.SerializedName

data class PerformerDetailDTO(
    @SerializedName("id")
    val id: String,

    @SerializedName("employee_id")
    val employeeId: String
)
