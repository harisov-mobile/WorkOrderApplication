package ru.internetcloud.workorderapplication.domain.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class MessageDialogMode : Parcelable {
    ERROR,
    INFO,
    SUCCESS
}
