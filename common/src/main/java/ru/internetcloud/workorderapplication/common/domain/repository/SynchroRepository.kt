package ru.internetcloud.workorderapplication.common.domain.repository

import ru.internetcloud.workorderapplication.common.domain.common.FunctionResult
import ru.internetcloud.workorderapplication.common.domain.common.UpdateState

interface SynchroRepository {

    suspend fun uploadWorkOrderById(id: String): FunctionResult // на сервер 1С выгрузить один ордер

    suspend fun sendWorkOrderToEmail(id: String, email: String): FunctionResult

    // переделываю:
    suspend fun updateData(): UpdateState

    suspend fun loadMockData()
}
