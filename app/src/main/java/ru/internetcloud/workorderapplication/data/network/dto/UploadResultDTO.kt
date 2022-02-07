package ru.internetcloud.workorderapplication.data.network.dto

import com.google.gson.annotations.SerializedName

data class UploadResultDTO(
    @SerializedName("success")
    val isSuccess: Boolean,

    @SerializedName("error_message")
    val errorMessage: String
)
