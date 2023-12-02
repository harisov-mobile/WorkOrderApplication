package ru.internetcloud.workorderapplication.presentation.workorder.detail

sealed interface WorkOrderDetailScreenEvent {

    data class ShowMessage(val message: String) : WorkOrderDetailScreenEvent

    data class ShowFieldsError(
        val errorInputNumber: Boolean = false,
        val errorDuplicateNumber: Boolean = false,
        val errorInputEmail: Boolean = false,
        val errorInputPerformer: Boolean = false
        // и так далее остальные поля если надо...
    ) : WorkOrderDetailScreenEvent

    object ShowSavingSuccess : WorkOrderDetailScreenEvent
}
