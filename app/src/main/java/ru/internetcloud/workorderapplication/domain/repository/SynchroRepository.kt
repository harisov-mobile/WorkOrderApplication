package ru.internetcloud.workorderapplication.domain.repository

import ru.internetcloud.workorderapplication.domain.common.FunctionResult

interface SynchroRepository {

    suspend fun getModifiedWorkOrdersQuantity(): Int

    suspend fun loadWorkOrders(): Boolean // из сервера 1С

    suspend fun uploadWorkOrders(): FunctionResult // на сервер 1С выгрузить все модифицированные ордеры

    suspend fun uploadWorkOrderById(id: String): FunctionResult // на сервер 1С выгрузить один ордер

    suspend fun loadDefaultWorkOrderSettings(): Boolean // из сервера 1С

    suspend fun sendWorkOrderToEmail(id: String, email: String): FunctionResult

    suspend fun deleteAllJobDetails()

    suspend fun deleteAllPerformers()

    suspend fun deleteAllWorkOrders()
}
