package ru.internetcloud.workorderapplication.data.network.dto

import com.google.gson.annotations.SerializedName

data class DefaultWorkOrderSettingsDTO(
    @SerializedName("id_department")
    val departmentId: String,

    @SerializedName("id_master")
    val masterId: String
)
