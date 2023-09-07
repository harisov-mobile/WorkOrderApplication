package ru.internetcloud.workorderapplication.presentation.login

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UiLoginState(
    val loginParams: LoginParams,
    val loading: Boolean = true,
    val entering: Boolean = false,
    val canContinue: Boolean = false,
    val canContinueDemoMode: Boolean = false,
    val errorInputServer: Boolean = false,
    val errorInputLogin: Boolean = false,
    val errorInputPassword: Boolean = false,
) : Parcelable
