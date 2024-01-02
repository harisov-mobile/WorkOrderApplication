package ru.internetcloud.workorderapplication.common.data.network.dto

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("is_authorized")
    var isAuthorized: Boolean
)
