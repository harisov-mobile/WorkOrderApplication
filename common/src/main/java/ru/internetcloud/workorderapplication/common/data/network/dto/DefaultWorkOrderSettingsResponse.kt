package ru.internetcloud.workorderapplication.common.data.network.dto

import com.google.gson.annotations.SerializedName

data class DefaultWorkOrderSettingsResponse(
    @SerializedName("settings")
    val settings: List<DefaultWorkOrderSettingsDTO>
)
