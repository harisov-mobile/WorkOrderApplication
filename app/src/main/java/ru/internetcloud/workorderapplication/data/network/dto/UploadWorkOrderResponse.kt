package ru.internetcloud.workorderapplication.data.network.dto

import com.google.gson.annotations.SerializedName

data class UploadWorkOrderResponse(
    @SerializedName("upload_result")
    val uploadResult: UploadResultDTO
)
