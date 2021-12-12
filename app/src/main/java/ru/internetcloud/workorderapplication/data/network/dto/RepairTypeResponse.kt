package ru.internetcloud.workorderapplication.data.network.dto

import com.google.gson.annotations.SerializedName

data class RepairTypeResponse(
    @SerializedName("repairtypes")
    val repairtypes: List<RepairTypeDTO>
)
