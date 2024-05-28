package ru.internetcloud.workorderapplication.common.domain.common

data class FunctionResult(
    var isSuccess: Boolean = false,
    var errorMessage: String = "",
    var amountOfModifiedWorkOrders: Int = 0
)
