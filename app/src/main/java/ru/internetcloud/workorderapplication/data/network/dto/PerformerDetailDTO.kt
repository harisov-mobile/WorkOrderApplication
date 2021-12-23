package ru.internetcloud.workorderapplication.data.network.dto

import com.google.gson.annotations.SerializedName

data class PerformerDetailDTO(
    @SerializedName("id")
    val id: String,

    @SerializedName("employee_id")
    val employeeId: String,

    @SerializedName("work_order_id")
    val workOrderId: String
)
