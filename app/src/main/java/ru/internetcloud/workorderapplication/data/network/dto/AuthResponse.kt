package ru.internetcloud.workorderapplication.data.network.dto

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("is_authorized")
    var isAuthorized: Boolean
)
