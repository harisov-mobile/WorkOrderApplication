package ru.internetcloud.workorderapplication.domain.common

sealed class UpdateState {

    data class Success(val modifiedWorkOrderNumber: Int) : UpdateState()

    object Loading : UpdateState()

    data class ContinueWithoutSynchro(val exception: Exception) : UpdateState()

    data class Error(val exception: Exception) : UpdateState()

    override fun toString(): String {
        return when (this) {
            is Success -> "Success[newWorkOrderNumber = $modifiedWorkOrderNumber]"
            is Error -> "Error[exception=$exception]"
            is Loading -> "Loading"
            is ContinueWithoutSynchro -> "ContinueWithoutSynchro"
        }
    }
}
