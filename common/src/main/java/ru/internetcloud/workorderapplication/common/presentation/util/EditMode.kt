package ru.internetcloud.workorderapplication.common.presentation.util

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class EditMode : Parcelable {

    object Add : EditMode()

    object Edit : EditMode()
}
