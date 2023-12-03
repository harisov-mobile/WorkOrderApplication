package ru.internetcloud.workorderapplication.presentation.util

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class ReturnResult : Parcelable {

    class Changed(val workOrderId: String) : ReturnResult()

    object NoOperation : ReturnResult()
}
