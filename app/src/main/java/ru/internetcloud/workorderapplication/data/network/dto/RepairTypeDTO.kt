package ru.internetcloud.workorderapplication.data.network.dto

import com.google.gson.annotations.SerializedName

data class RepairTypeDTO(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String
)
