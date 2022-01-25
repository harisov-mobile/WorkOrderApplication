package ru.internetcloud.workorderapplication.domain.repository

import ru.internetcloud.workorderapplication.domain.common.FunctionResult

interface SynchroRepository {
    suspend fun getModifiedWorkOrdersQuantity(): Int

    suspend fun loadWorkOrders(): Boolean // из сервера 1С

    suspend fun uploadWorkOrders(): FunctionResult // на сервер 1С выгрузить
}
