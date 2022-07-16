package ru.internetcloud.workorderapplication.domain.repository

import ru.internetcloud.workorderapplication.domain.common.FunctionResult
import ru.internetcloud.workorderapplication.presentation.synchro.UpdateState

interface SynchroRepository {

    suspend fun uploadWorkOrderById(id: String): FunctionResult // на сервер 1С выгрузить один ордер

    suspend fun sendWorkOrderToEmail(id: String, email: String): FunctionResult

    // переделываю:
    suspend fun updateData(): UpdateState

    suspend fun loadMockData()
}
