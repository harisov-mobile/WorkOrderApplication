package ru.internetcloud.workorderapplication.data.network.dto

import com.google.gson.annotations.SerializedName

data class WorkOrderDTO(
    @SerializedName("id")
    val id: String,

    @SerializedName("number")
    val number: String,

    @SerializedName("date")
    val dateString: String,

    @SerializedName("partner_id")
    val partnerId: String,

    @SerializedName("car_id")
    val carId: String,

    @SerializedName("repair_type_id")
    val repairTypeId: String,

    @SerializedName("department_id")
    val departmentId: String,

    @SerializedName("request_reason")
    val requestReason: String,

    @SerializedName("master_id")
    val masterId: String,

    @SerializedName("comment")
    val comment: String

    // а сумма документа???
)
