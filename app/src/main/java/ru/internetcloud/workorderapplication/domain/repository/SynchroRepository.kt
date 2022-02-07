package ru.internetcloud.workorderapplication.domain.repository

import ru.internetcloud.workorderapplication.domain.common.FunctionResult
import ru.internetcloud.workorderapplication.domain.document.WorkOrder

interface SynchroRepository {
    suspend fun getModifiedWorkOrdersQuantity(): Int

    suspend fun loadWorkOrders(): Boolean // из сервера 1С

    suspend fun uploadWorkOrders(): FunctionResult // на сервер 1С выгрузить все модифицированные ордеры

    suspend fun uploadWorkOrderById(id: String): FunctionResult // на сервер 1С выгрузить один ордер

    suspend fun loadDefaultWorkOrderSettings(): Boolean // из сервера 1С

    suspend fun sendWorkOrderByEmail(workOrder: WorkOrder): FunctionResult
}
