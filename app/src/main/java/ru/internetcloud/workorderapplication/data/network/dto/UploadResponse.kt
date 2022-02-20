package ru.internetcloud.workorderapplication.data.network.dto

import com.google.gson.annotations.SerializedName

data class UploadResponse(
    @SerializedName("upload_result")
    val uploadResult: UploadResultDTO
)
