package ru.internetcloud.workorderapplication.domain.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class ScreenMode : Parcelable {
    ADD,
    EDIT
}
