package ru.internetcloud.workorderapplication.presentation.logon

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UiLoginState(
    val server: String = "",
    val login: String = "",
    val password: String = "",
    val loading: Boolean = true,
    val canContinue: Boolean = false,
    val canContinueDemoMode: Boolean = false,
    val errorInputServer: Boolean = false,
    val errorInputLogin: Boolean = false,
    val errorInputPassword: Boolean = false,
    val errorAuthorization: Boolean = false,
    val errorMessage: String = ""
) : Parcelable
