package ru.internetcloud.workorderapplication.workorders.presentation.navigation

import ru.internetcloud.workorderapplication.common.presentation.util.EditMode

data class ToWorkOrderArgs(
    val workOrderId: String,
    val editMode: EditMode,
    val requestKeyReturnResult: String,
    val argNameReturnResult: String
)
