package ru.internetcloud.workorderapplication.login.presentation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginParams(
    val server: String = "",
    val login: String = "",
    val password: String = ""
) : Parcelable
