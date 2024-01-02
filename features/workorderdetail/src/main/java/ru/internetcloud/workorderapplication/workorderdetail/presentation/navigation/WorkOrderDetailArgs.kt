package ru.internetcloud.workorderapplication.workorderdetail.presentation.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.internetcloud.workorderapplication.common.presentation.util.EditMode

@Parcelize
data class WorkOrderDetailArgs(
    val workOrderId: String,
    val editMode: EditMode,
    val requestKeyReturnResult: String,
    val argNameReturnResult: String
) : Parcelable
