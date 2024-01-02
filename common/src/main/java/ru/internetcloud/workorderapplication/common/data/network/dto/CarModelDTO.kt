package ru.internetcloud.workorderapplication.common.data.network.dto

import com.google.gson.annotations.SerializedName

data class CarModelDTO(
    @SerializedName("id")
    val id: String,

    @SerializedName("code_1c")
    val code1C: String,

    @SerializedName("name")
    val name: String
)
